package unithon.helpjob.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import unithon.helpjob.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val _signInEvent = MutableSharedFlow<SignInEvent>()
    val signInEvent: SharedFlow<SignInEvent> = _signInEvent.asSharedFlow()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun signIn() {
        if (!uiState.value.isInputValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.signIn(
                    email = uiState.value.email,
                    password = uiState.value.password
                )
                _signInEvent.emit(SignInEvent.NavigateToMain)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userMessage = e.message ?: "로그인에 실패했습니다"
                    )
                }
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}

sealed class SignInEvent {
    object NavigateToMain : SignInEvent()
}