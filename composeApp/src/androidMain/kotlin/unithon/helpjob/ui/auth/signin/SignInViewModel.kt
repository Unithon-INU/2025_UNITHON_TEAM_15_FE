package unithon.helpjob.ui.auth.signin

import androidx.lifecycle.viewModelScope
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.error_empty_email
import helpjob.composeapp.generated.resources.error_empty_password
import helpjob.composeapp.generated.resources.error_invalid_email
import helpjob.composeapp.generated.resources.error_short_password
import helpjob.composeapp.generated.resources.sign_in_email_not_found
import helpjob.composeapp.generated.resources.sign_in_failed
import helpjob.composeapp.generated.resources.sign_in_password_wrong
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import timber.log.Timber
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.EmailNotFoundException
import unithon.helpjob.data.repository.WrongPasswordException
import unithon.helpjob.ui.base.BaseViewModel
import unithon.helpjob.util.Analytics
import unithon.helpjob.util.EmailValidator

class SignInViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    data class SignInUiState(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val isSignInSuccessful: Boolean = false,
        val shouldGoToHome: Boolean = false,
        val emailError: Boolean = false,
        val passwordError: Boolean = false,
        val emailErrorMessage: StringResource? = null,
        val passwordErrorMessage: StringResource? = null
    ) {
        val isInputValid: Boolean
            get() = email.isNotBlank() && password.length >= 6 &&
                    EmailValidator.isValid(email)
    }

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    // 🆕 스낵바 메시지용 SharedFlow - 일회성 이벤트
    private val _snackbarMessage = MutableSharedFlow<StringResource>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

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
                    emailErrorMessage = Res.string.error_empty_email
                )
            }
            hasError = true
        } else if (!EmailValidator.isValid(currentState.email)) {
            _uiState.update {
                it.copy(
                    emailError = true,
                    emailErrorMessage = Res.string.error_invalid_email
                )
            }
            hasError = true
        }

        if (currentState.password.isBlank()) {
            _uiState.update {
                it.copy(
                    passwordError = true,
                    passwordErrorMessage = Res.string.error_empty_password
                )
            }
            hasError = true
        } else if (currentState.password.length < 6) {
            _uiState.update {
                it.copy(
                    passwordError = true,
                    passwordErrorMessage = Res.string.error_short_password
                )
            }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch(crashPreventionHandler) {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // 로그인
                authRepository.signIn(
                    email = currentState.email,
                    password = currentState.password
                )

                // 온보딩 완료 여부 체크
                val isCompleted = authRepository.isOnboardingCompleted()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSignInSuccessful = !isCompleted, // 온보딩 미완료면 온보딩으로
                        shouldGoToHome = isCompleted // 온보딩 완료면 홈으로
                    )
                }
                Analytics.logEvent("user_login")
            } catch (e: EmailNotFoundException) {
                // 이메일 관련 에러는 필드 에러로 표시
                Timber.d(e, "Sign in failed - email not found")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        emailError = true,
                        emailErrorMessage = Res.string.sign_in_email_not_found
                    )
                }
            } catch (e: WrongPasswordException) {
                // 비밀번호 관련 에러는 필드 에러로 표시
                Timber.d(e, "Sign in failed - wrong password")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        passwordError = true,
                        passwordErrorMessage = Res.string.sign_in_password_wrong
                    )
                }
            } catch (e: Exception) {
                // 6. Critical Error - 스낵바 + 로깅
                Timber.e(e, "Sign in failed - unexpected error")
                _uiState.update { it.copy(isLoading = false) }
                _snackbarMessage.emit(Res.string.sign_in_failed)
            }
        }
    }
}