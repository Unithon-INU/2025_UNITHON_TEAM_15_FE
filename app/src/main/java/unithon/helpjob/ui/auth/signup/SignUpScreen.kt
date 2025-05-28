package unithon.helpjob.ui.auth.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.theme.*

@Composable
fun SignUpScreen(
    onNavigateToNicknameSetup: () -> Unit,
    onBack: () -> Unit, // 🆕 뒤로가기 추가
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // 회원가입 성공시 네비게이션
    LaunchedEffect(uiState.isSignUpSuccessful) {
        if (uiState.isSignUpSuccessful) {
            onNavigateToNicknameSetup()
        }
    }

    // 🆕 SharedFlow로 단발성 에러 이벤트 처리 (네트워크 에러 등)
    LaunchedEffect(Unit) {
        viewModel.errorEvents.collect { messageResId ->
            val message = context.getString(messageResId) // stringResource 대신 이걸로
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            HelpJobTopAppBar(
                title = R.string.sign_up_top_bar_title,
                onBack = onBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(40.dp)) // 🆕 상단바가 있어서 간격 조정

            // 제목
            Text(
                text = stringResource(id = R.string.sign_up_title),
                style = MaterialTheme.typography.headlineLarge,
                color = Grey700
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 부제목
            Text(
                text = stringResource(id = R.string.sign_up_subtitle),
                style = MaterialTheme.typography.titleMedium,
                color = Grey500
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 이메일 레이블
            Text(
                text = stringResource(id = R.string.sign_up_email_label),
                style = MaterialTheme.typography.titleSmall,
                color = Grey500
            )

            Spacer(modifier = Modifier.height(9.dp))

            // 🆕 필드별 에러 표시
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
                style = MaterialTheme.typography.titleSmall,
                color = Grey500
            )

            Spacer(modifier = Modifier.height(9.dp))

            // 🆕 비밀번호 토글 + 필드별 에러 표시
            HelpJobTextField(
                value = uiState.password,
                onValueChange = viewModel::updatePassword,
                label = "",
                placeholder = stringResource(id = R.string.sign_up_password_hint),
                isPassword = true, // 🆕 비밀번호 필드로 설정
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
    }
}