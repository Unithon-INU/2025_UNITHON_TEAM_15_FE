package unithon.helpjob.ui.auth.nickname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import unithon.helpjob.R
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.NicknameDuplicateException
import unithon.helpjob.data.repository.SignUpDataRepository
import javax.inject.Inject

@HiltViewModel
class NicknameSetupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val signUpDataRepository: SignUpDataRepository
) : ViewModel() {

    data class NicknameSetupUiState(
        val nickname: String = "",
        val nicknameLength: Int = 0,
        val isLoading: Boolean = false,
        val isNicknameSet: Boolean = false,
        val nicknameError: Boolean = false,
        val nicknameErrorMessage: Int? = null
    ) {
        val isInputValid: Boolean
            get() = nickname.length in 2..10 && !nicknameError
    }

    private val _uiState = MutableStateFlow(NicknameSetupUiState())
    val uiState: StateFlow<NicknameSetupUiState> = _uiState.asStateFlow()

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
                        nicknameErrorMessage = R.string.nickname_empty_error
                    )
                }
                return
            }
            currentState.nickname.length < 2 -> {
                _uiState.update {
                    it.copy(
                        nicknameError = true,
                        nicknameErrorMessage = R.string.nickname_too_short_error
                    )
                }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // 1. 저장된 회원가입 데이터 가져오기
                val signUpData = signUpDataRepository.getSignUpData()

                if (signUpData == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nicknameError = true,
                            nicknameErrorMessage = R.string.nickname_setup_failed
                        )
                    }
                    return@launch
                }

                // 2. 회원가입 + 닉네임 설정 배치 처리
                authRepository.signUp(signUpData.email, signUpData.password)
                authRepository.setNickname(currentState.nickname)

                // 3. 임시 데이터 정리
                signUpDataRepository.clearSignUpData()

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
                        nicknameErrorMessage = R.string.nickname_duplicate_error
                    )
                }
            } catch (e: Exception) {
                // ✅ 수정: SharedFlow emit 대신 UI 상태로 에러 처리
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        nicknameError = true,
                        nicknameErrorMessage = R.string.nickname_setup_failed
                    )
                }
            }
        }
    }
}