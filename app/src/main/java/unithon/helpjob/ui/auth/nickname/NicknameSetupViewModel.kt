package unithon.helpjob.ui.auth.nickname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import unithon.helpjob.R
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.NicknameDuplicateException
import javax.inject.Inject

@HiltViewModel
class NicknameSetupViewModel @Inject constructor(
    private val authRepository: AuthRepository
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

    // 🆕 단발성 에러 이벤트를 위한 SharedFlow
    private val _errorEvents = MutableSharedFlow<Int>()
    val errorEvents: SharedFlow<Int> = _errorEvents.asSharedFlow()

    fun updateNickname(nickname: String) {
        if (nickname.length > 10) return  // 10자 제한

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
                authRepository.setNickname(currentState.nickname)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isNicknameSet = true
                    )
                }
            } catch (e: NicknameDuplicateException) {
                // 🆕 닉네임 중복은 닉네임 필드와 1:1 매핑 → 필드별 표시
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        nicknameError = true,
                        nicknameErrorMessage = R.string.nickname_duplicate_error
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false)
                }
                // 🆕 네트워크 에러 등은 SharedFlow로 전송
                _errorEvents.emit(R.string.nickname_setup_failed)
            }
        }
    }
}