package unithon.helpjob.ui.auth.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.data.repository.LanguageAwareScreen
import unithon.helpjob.ui.auth.components.AuthEmailTextField
import unithon.helpjob.ui.auth.components.AuthPasswordTextField
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.HelpJobTheme
import unithon.helpjob.ui.theme.Primary600

@Composable
fun SignInScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // 스낵바 메시지 처리
    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.collect { messageRes ->
            snackbarHostState.showSnackbar(
                message = context.getString(messageRes)
            )
        }
    }

    // 로그인 성공시 네비게이션
    LaunchedEffect(uiState.isSignInSuccessful) {
        if (uiState.isSignInSuccessful) {
            onNavigateToOnboarding()
        }
    }

    // 홈으로 네비게이션
    LaunchedEffect(uiState.shouldGoToHome) {
        if (uiState.shouldGoToHome) {
            onNavigateToHome()
        }
    }

    SignInContent(
        uiState = uiState,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onSignInClick = viewModel::signIn,
        onNavigateToSignUp = onNavigateToSignUp,
        modifier = modifier
    )
}

/**
 * SignIn UI 컨텐츠 (프리뷰 지원을 위해 분리)
 */
@Composable
private fun SignInContent(
    uiState: SignInViewModel.SignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    LanguageAwareScreen {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.sign_in_welcome_main),
                style = MaterialTheme.typography.headlineLarge,
                color = Grey700
            )

            Text(
                text = stringResource(id = R.string.sign_in_welcome_sub),
                style = MaterialTheme.typography.headlineMedium,
                color = Grey700
            )

            Spacer(modifier = Modifier.height(39.dp))

            AuthEmailTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                labelText = stringResource(R.string.sign_in_email_label),
                placeholderText = stringResource(R.string.sign_in_email_hint),
                isError = uiState.emailError,
                errorMessage = uiState.emailErrorMessage?.let { stringResource(id = it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            AuthPasswordTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                labelText = stringResource(R.string.sign_in_password_label),
                placeholderText = stringResource(id = R.string.sign_in_password_hint),
                isError = uiState.passwordError,
                errorMessage = uiState.passwordErrorMessage?.let { stringResource(id = it) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(46.dp))

            // 로그인 버튼
            HelpJobButton(
                text = stringResource(id = R.string.sign_in_button),
                onClick = onSignInClick,
                enabled = uiState.isInputValid,
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(38.dp))

            // 또는 구분선
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Grey300)
                )

                Spacer(modifier = Modifier.width(22.dp))

                Text(
                    text = stringResource(id = R.string.sign_in_or_divider),
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey300
                )

                Spacer(modifier = Modifier.width(22.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Grey300)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 회원가입 링크
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.sign_in_no_account),
                    style = MaterialTheme.typography.bodySmall,
                    color = Grey600
                )
                Spacer(modifier = Modifier.width(11.dp))
                Text(
                    text = stringResource(id = R.string.sign_in_go_to_sign_up),
                    style = MaterialTheme.typography.titleSmall,
                    color = Primary600,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SignInPreview() {
    HelpJobTheme {
        SignInContent(
            uiState = SignInViewModel.SignInUiState(
                email = "test@example.com",
                password = "password123",
                isLoading = false,
                emailError = false,
                passwordError = false,
                emailErrorMessage = null,
                passwordErrorMessage = null
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onSignInClick = {},
            onNavigateToSignUp = {},
            modifier = Modifier
        )
    }
}

// ✅ 추가 프리뷰 - 에러 상태
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SignInErrorPreview() {
    HelpJobTheme {
        SignInContent(
            uiState = SignInViewModel.SignInUiState(
                email = "invalid-email",
                password = "dlwnsgml1234",
                isLoading = false,
                emailError = true,
                passwordError = true,
                emailErrorMessage = R.string.error_invalid_email,
                passwordErrorMessage = R.string.error_short_password
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onSignInClick = {},
            onNavigateToSignUp = {},
            modifier = Modifier
        )
    }
}