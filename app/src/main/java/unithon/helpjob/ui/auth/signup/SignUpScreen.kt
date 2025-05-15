package unithon.helpjob.ui.auth.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.*

@Composable
fun SignUpScreen(
    onNavigateToSignIn: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 회원가입 성공시 네비게이션
    LaunchedEffect(uiState.isSignUpSuccessful) {
        if (uiState.isSignUpSuccessful) {
            onNavigateToOnboarding()
        }
    }

    // 에러 메시지 처리
    uiState.userMessage?.let { message ->
        val snackbarText = stringResource(message)
        LaunchedEffect(snackbarHostState, viewModel, message, snackbarText) {
            snackbarHostState.showSnackbar(snackbarText)
            viewModel.userMessageShown()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // 제목
            Text(
                text = stringResource(id = R.string.sign_up_title),
                style = MaterialTheme.typography.headlineLarge, // 24sp, Bold
                color = Grey700
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 부제목
            Text(
                text = stringResource(id = R.string.sign_up_subtitle),
                style = MaterialTheme.typography.titleMedium, // 16sp, Bold (Title1)
                color = Grey500
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 이메일 레이블
            Text(
                text = stringResource(id = R.string.sign_up_email_label),
                style = MaterialTheme.typography.titleSmall, // 14sp, Bold
                color = Grey500
            )

            Spacer(modifier = Modifier.height(9.dp))

            // 이메일 입력 필드
            HelpJobTextField(
                value = uiState.email,
                onValueChange = viewModel::updateEmail,
                label = "",
                placeholder = stringResource(id = R.string.sign_up_email_hint),
                isError = uiState.emailError,
                errorMessage = uiState.emailErrorMessage?.let { stringResource(id = it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 비밀번호 레이블
            Text(
                text = stringResource(id = R.string.sign_up_password_label),
                style = MaterialTheme.typography.titleSmall, // 14sp, Bold
                color = Grey500
            )

            Spacer(modifier = Modifier.height(9.dp))

            // 비밀번호 입력 필드
            HelpJobTextField(
                value = uiState.password,
                onValueChange = viewModel::updatePassword,
                label = "",
                placeholder = stringResource(id = R.string.sign_up_password_hint),
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.passwordError,
                errorMessage = uiState.passwordErrorMessage?.let { stringResource(id = it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // 다음 버튼
            HelpJobButton(
                text = stringResource(id = R.string.sign_up_next_button),
                onClick = viewModel::signUp,
                enabled = uiState.isInputValid,
                isLoading = uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )
        }

        // SnackbarHost 추가
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}