package unithon.helpjob.ui.auth.signin

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val userMessage: String? = null
) {
    val isInputValid: Boolean
        get() = email.isNotBlank() && password.length >= 6 &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}