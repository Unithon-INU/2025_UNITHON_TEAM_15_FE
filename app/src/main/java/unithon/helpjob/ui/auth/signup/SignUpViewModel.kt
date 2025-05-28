package unithon.helpjob.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import unithon.helpjob.R
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.EmailAlreadyInUseException
import unithon.helpjob.data.repository.EmailCodeExpiredException
import unithon.helpjob.data.repository.EmailVerificationFailedException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    data class SignUpUiState(
        val email: String = "",
        val password: String = "",
        val verificationCode: String = "",
        val isLoading: Boolean = false,
        val isSignUpSuccessful: Boolean = false,

        // 이메일 관련 상태
        val emailError: Boolean = false,
        val emailErrorMessage: Int? = null,
        val isEmailSent: Boolean = false,
        val isSendingEmail: Boolean = false,

        // 비밀번호 관련 상태
        val passwordError: Boolean = false,
        val passwordErrorMessage: Int? = null,

        // 인증코드 관련 상태
        val verificationCodeError: Boolean = false,
        val verificationCodeErrorMessage: Int? = null,
        val isVerifyingCode: Boolean = false,
        val isCodeVerified: Boolean = false
    ) {
        val isEmailValid: Boolean
            get() = email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        val isPasswordValid: Boolean
            get() = password.length >= 6

        val isInputValid: Boolean
            get() = isEmailValid && isPasswordValid && isCodeVerified
    }

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = false,
                emailErrorMessage = null,
                // 이메일이 변경되면 인증 상태 초기화
                isEmailSent = false,
                isCodeVerified = false,
                verificationCode = "",
                verificationCodeError = false,
                verificationCodeErrorMessage = null
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = false,
                passwordErrorMessage = null
            )
        }
    }

    fun updateVerificationCode(code: String) {
        _uiState.update {
            it.copy(
                verificationCode = code,
                verificationCodeError = false,
                verificationCodeErrorMessage = null
            )
        }
    }

    fun sendEmailVerification() {
        val currentState = uiState.value

        // 이메일 유효성 검사
        if (currentState.email.isBlank()) {
            _uiState.update {
                it.copy(
                    emailError = true,
                    emailErrorMessage = R.string.error_empty_email
                )
            }
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _uiState.update {
                it.copy(
                    emailError = true,
                    emailErrorMessage = R.string.error_invalid_email
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSendingEmail = true) }
            try {
                authRepository.sendEmailVerification(currentState.email)
                _uiState.update {
                    it.copy(
                        isSendingEmail = false,
                        isEmailSent = true,
                        emailError = false,
                        emailErrorMessage = null
                    )
                }
            } catch (e: EmailAlreadyInUseException) {
                _uiState.update {
                    it.copy(
                        isSendingEmail = false,
                        emailError = true,
                        emailErrorMessage = R.string.sign_up_email_exists
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSendingEmail = false,
                        emailError = true,
                        emailErrorMessage = R.string.email_send_failed
                    )
                }
            }
        }
    }

    fun verifyEmailCode() {
        val currentState = uiState.value

        if (currentState.verificationCode.isBlank()) {
            _uiState.update {
                it.copy(
                    verificationCodeError = true,
                    verificationCodeErrorMessage = R.string.error_empty_verification_code
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isVerifyingCode = true) }
            try {
                authRepository.verifyEmailCode(currentState.email, currentState.verificationCode)
                _uiState.update {
                    it.copy(
                        isVerifyingCode = false,
                        isCodeVerified = true,
                        verificationCodeError = false,
                        verificationCodeErrorMessage = null
                    )
                }
            } catch (e: EmailCodeExpiredException) {
                _uiState.update {
                    it.copy(
                        isVerifyingCode = false,
                        verificationCodeError = true,
                        verificationCodeErrorMessage = R.string.verification_code_expired
                    )
                }
            } catch (e: EmailVerificationFailedException) {
                _uiState.update {
                    it.copy(
                        isVerifyingCode = false,
                        verificationCodeError = true,
                        verificationCodeErrorMessage = R.string.verification_code_invalid
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isVerifyingCode = false,
                        verificationCodeError = true,
                        verificationCodeErrorMessage = R.string.verification_failed
                    )
                }
            }
        }
    }

    fun resendEmailVerification() {
        sendEmailVerification()
    }

    fun signUp() {
        val currentState = uiState.value

        // 최종 입력 검증
        var hasError = false

        if (!currentState.isEmailValid) {
            _uiState.update {
                it.copy(
                    emailError = true,
                    emailErrorMessage = R.string.error_invalid_email
                )
            }
            hasError = true
        }

        if (!currentState.isPasswordValid) {
            _uiState.update {
                it.copy(
                    passwordError = true,
                    passwordErrorMessage = R.string.error_short_password
                )
            }
            hasError = true
        }

        if (!currentState.isCodeVerified) {
            _uiState.update {
                it.copy(
                    verificationCodeError = true,
                    verificationCodeErrorMessage = R.string.email_verification_required
                )
            }
            return
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
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        passwordError = true,
                        passwordErrorMessage = R.string.sign_up_failed
                    )
                }
            }
        }
    }
}