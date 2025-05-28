package unithon.helpjob.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import unithon.helpjob.R
import unithon.helpjob.data.model.response.ErrorResponse
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
        val emailErrorMessage: Int? = null,
        val passwordErrorMessage: Int? = null
    ) {
        val isInputValid: Boolean
            get() = email.isNotBlank() && password.length >= 6 &&
                    android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true }

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
                handleSignInError(e)
            }
        }
    }

    private fun handleSignInError(exception: Exception) {
        val errorMessage = exception.message ?: ""

        // 서버 에러 응답 JSON 파싱 시도
        val errorResponse = try {
            if (errorMessage.startsWith("{")) {
                json.decodeFromString<ErrorResponse>(errorMessage)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }

        when (errorResponse?.code) {
            "404-1" -> {
                // 해당 회원이 존재하지 않습니다
                _uiState.update {
                    it.copy(
                        emailError = true,
                        emailErrorMessage = R.string.sign_in_email_not_found
                    )
                }
            }
            "401-5" -> {
                // 비밀번호가 일치하지 않습니다
                _uiState.update {
                    it.copy(
                        passwordError = true,
                        passwordErrorMessage = R.string.sign_in_password_wrong
                    )
                }
            }
            else -> {
                // 기타 네트워크 에러 등 또는 JSON 파싱 실패
                _uiState.update {
                    it.copy(
                        passwordError = true,
                        passwordErrorMessage = R.string.sign_in_failed
                    )
                }
            }
        }
    }
}