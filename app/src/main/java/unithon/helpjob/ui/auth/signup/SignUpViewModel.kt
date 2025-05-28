package unithon.helpjob.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import unithon.helpjob.R
import unithon.helpjob.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    data class SignUpUiState(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val isSignUpSuccessful: Boolean = false,
        val emailError: Boolean = false,
        val passwordError: Boolean = false,
        val emailErrorMessage: Int? = null,
        val passwordErrorMessage: Int? = null
    ) {
        val isInputValid: Boolean
            get() = email.isNotBlank() && password.length >= 6 &&
                    android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    // 🆕 단발성 에러 이벤트 (시스템 에러용)
    private val _errorEvents = MutableSharedFlow<Int>()
    val errorEvents: SharedFlow<Int> = _errorEvents.asSharedFlow()

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = false,
                emailErrorMessage = null,
                // 🆕 다른 필드 입력 시 서버 에러도 클리어
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
                // 🆕 다른 필드 입력 시 서버 에러도 클리어
                emailError = false,
                emailErrorMessage = null
            )
        }
    }

    fun signUp() {
        val currentState = uiState.value

        // 입력 검증 - 각 필드별로 개별 에러 설정
        var hasError = false

        if (currentState.email.isBlank()) {
            _uiState.update {
                it.copy(
                    emailError = true,
                    emailErrorMessage = R.string.error_empty_email
                )
            }
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _uiState.update {
                it.copy(
                    emailError = true,
                    emailErrorMessage = R.string.error_invalid_email
                )
            }
            hasError = true
        }

        if (currentState.password.isBlank()) {
            _uiState.update {
                it.copy(
                    passwordError = true,
                    passwordErrorMessage = R.string.error_empty_password
                )
            }
            hasError = true
        } else if (currentState.password.length < 6) {
            _uiState.update {
                it.copy(
                    passwordError = true,
                    passwordErrorMessage = R.string.error_short_password
                )
            }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.signUp(
                    email = currentState.email,
                    password = currentState.password
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSignUpSuccessful = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }

                when {
                    e.message?.contains("already exists") == true -> {
                        // 🆕 이메일 중복은 이메일 필드와 1:1 매핑 → 필드별 표시
                        _uiState.update {
                            it.copy(
                                emailError = true,
                                emailErrorMessage = R.string.sign_up_email_exists
                            )
                        }
                    }
                    else -> {
                        // 🆕 기타 시스템 에러 → 스낵바
                        _errorEvents.emit(R.string.sign_up_failed)
                    }
                }
            }
        }
    }
}