package unithon.helpjob.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import unithon.helpjob.R
import unithon.helpjob.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    data class SignInUiState(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val isSignInSuccessful: Boolean = false,
        val emailError: Boolean = false,
        val passwordError: Boolean = false,
        val emailErrorMessage: String? = null,
        val passwordErrorMessage: String? = null
    ) {
        val isInputValid: Boolean
            get() = email.isNotBlank() && password.length >= 6 &&
                    android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    // ✅ SharedFlow 완전 제거 - 모든 에러는 UiState로

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = false,
                emailErrorMessage = null,
                // 다른 필드 입력 시 서버 에러도 클리어
                passwordError = false,
                passwordErrorMessage = null
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = false,
                passwordErrorMessage = null,
                // 다른 필드 입력 시 서버 에러도 클리어
                emailError = false,
                emailErrorMessage = null
            )
        }
    }

    fun signIn() {
        val currentState = uiState.value

        // 클라이언트 validation
        var hasError = false

        if (currentState.email.isBlank()) {
            _uiState.update {
                it.copy(
                    emailError = true,
                    emailErrorMessage = "이메일을 입력해주세요"
                )
            }
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _uiState.update {
                it.copy(
                    emailError = true,
                    emailErrorMessage = "올바른 이메일 형식이 아닙니다"
                )
            }
            hasError = true
        }

        if (currentState.password.isBlank()) {
            _uiState.update {
                it.copy(
                    passwordError = true,
                    passwordErrorMessage = "비밀번호를 입력해주세요"
                )
            }
            hasError = true
        } else if (currentState.password.length < 6) {
            _uiState.update {
                it.copy(
                    passwordError = true,
                    passwordErrorMessage = "비밀번호는 6자 이상이어야 합니다"
                )
            }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.signIn(
                    email = currentState.email,
                    password = currentState.password
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSignInSuccessful = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }

                val serverMessage = e.message ?: "알 수 없는 오류가 발생했습니다"

                when (serverMessage) {
                    "해당 회원이 존재하지 않습니다." -> {
                        _uiState.update {
                            it.copy(
                                emailError = true,
                                emailErrorMessage = serverMessage
                            )
                        }
                    }

                    "비밀번호가 일치하지 않습니다." -> {
                        _uiState.update {
                            it.copy(
                                passwordError = true,
                                passwordErrorMessage = serverMessage
                            )
                        }
                    }

                    else -> {
                        // ✅ 네트워크 에러 등도 적절한 필드에 표시
                        // 보통 마지막 입력 필드 또는 일반적으로 비밀번호 필드에 표시
                        _uiState.update {
                            it.copy(
                                passwordError = true,
                                passwordErrorMessage = "로그인 중 오류가 발생했습니다. 다시 시도해주세요."
                            )
                        }
                    }
                }
            }
        }
    }
}