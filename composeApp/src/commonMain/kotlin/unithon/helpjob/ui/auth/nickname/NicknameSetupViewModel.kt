package unithon.helpjob.ui.auth.nickname

import androidx.lifecycle.viewModelScope
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.nickname_duplicate_error
import helpjob.composeapp.generated.resources.nickname_empty_error
import helpjob.composeapp.generated.resources.nickname_setup_failed
import helpjob.composeapp.generated.resources.nickname_too_short_error
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.EmploymentCheckRepository
import unithon.helpjob.data.repository.NicknameDuplicateException
import unithon.helpjob.data.repository.SignUpDataRepository
import unithon.helpjob.ui.base.BaseViewModel
import unithon.helpjob.util.Logger

class NicknameSetupViewModel(
    private val authRepository: AuthRepository,
    private val signUpDataRepository: SignUpDataRepository,
    private val employmentCheckRepository: EmploymentCheckRepository
) : BaseViewModel() {

    data class NicknameSetupUiState(
        val nickname: String = "",
        val nicknameLength: Int = 0,
        val isLoading: Boolean = false,
        val isNicknameSet: Boolean = false,
        val nicknameError: Boolean = false,
        val nicknameErrorMessage: StringResource? = null
    ) {
        val isInputValid: Boolean
            get() = nickname.length in 2..10 && !nicknameError
    }

    private val _uiState = MutableStateFlow(NicknameSetupUiState())
    val uiState: StateFlow<NicknameSetupUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<StringResource>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    fun updateNickname(nickname: String) {
        if (nickname.length > 10) return

        _uiState.update {
            it.copy(
                nickname = nickname,
                nicknameLength = nickname.length,
                nicknameError = false,
                nicknameErrorMessage = null
            )
        }
    }

    fun setNickname() {
        val currentState = uiState.value

        // 입력 검증
        when {
            currentState.nickname.isBlank() -> {
                _uiState.update {
                    it.copy(
                        nicknameError = true,
                        nicknameErrorMessage = Res.string.nickname_empty_error
                    )
                }
                return
            }
            currentState.nickname.length < 2 -> {
                _uiState.update {
                    it.copy(
                        nicknameError = true,
                        nicknameErrorMessage = Res.string.nickname_too_short_error
                    )
                }
                return
            }
        }

        viewModelScope.launch(crashPreventionHandler) {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // 1. 저장된 회원가입 데이터 가져오기
                val signUpData = signUpDataRepository.getSignUpData()

                if (signUpData == null) {
                    _snackbarMessage.emit(Res.string.nickname_setup_failed)
                    _uiState.update { it.copy(isLoading = false) }
                    println("[NicknameSetupViewModel] SignUp data is null")
                    return@launch
                }

                // 2. 회원가입 + 닉네임 설정 배치 처리
                authRepository.signUp(signUpData.email, signUpData.password)
                authRepository.setNickname(currentState.nickname)

                // 🆕 3. Guest 온보딩 데이터 동기화 (새 계정의 초기 데이터)
                val guestProfile = authRepository.getGuestProfile()
                if (guestProfile != null) {
                    authRepository.setProfile(
                        language = guestProfile.language,
                        languageLevel = guestProfile.languageLevel,
                        visaType = guestProfile.visaType,
                        industry = guestProfile.industry
                    )
                    Logger.d("✅ Guest 온보딩 데이터 동기화 완료")
                }

                // 🆕 4. Guest 체크리스트 동기화 (완료 대기)
                employmentCheckRepository.syncGuestDataToServer()
                Logger.d("✅ Guest 체크리스트 동기화 완료")

                // 5. 임시 데이터 정리
                signUpDataRepository.clearSignUpData()

                // 6. 모든 동기화 완료 후 UI 업데이트
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isNicknameSet = true
                    )
                }
            } catch (e: NicknameDuplicateException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        nicknameError = true,
                        nicknameErrorMessage = Res.string.nickname_duplicate_error
                    )
                }
            } catch (e: Exception) {
                _snackbarMessage.emit(Res.string.nickname_setup_failed)
                _uiState.update { it.copy(isLoading = false) }
                println("[NicknameSetupViewModel] Nickname setup failed: ${e.message}")
            }
        }
    }
}
