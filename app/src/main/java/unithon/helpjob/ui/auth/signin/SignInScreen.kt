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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.Primary600

@Composable
fun SignInScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 로그인 성공시 네비게이션
    LaunchedEffect(uiState.isSignInSuccessful) {
        if (uiState.isSignInSuccessful) {
            onNavigateToOnboarding()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.sign_in_welcome_main),
            style = MaterialTheme.typography.headlineLarge, // Headline1 - 24sp, Bold
            color = Grey700
        )

        Text(
            text = stringResource(id = R.string.sign_in_welcome_sub),
            style = MaterialTheme.typography.headlineMedium, // Headline2 - 20sp, Bold
            color = Grey700
        )

        Spacer(modifier = Modifier.height(39.dp))

        // 이메일 입력
        Text(
            text = stringResource(id = R.string.sign_in_email_label),
            style = MaterialTheme.typography.titleSmall,
            color = Grey500
        )

        Spacer(modifier = Modifier.height(9.dp))

        HelpJobTextField(
            value = uiState.email,
            onValueChange = viewModel::updateEmail,
            label = "",
            isError = uiState.emailError,
            errorMessage = uiState.emailErrorMessage?.let { stringResource(id = it) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 비밀번호 입력
        Text(
            text = stringResource(id = R.string.sign_in_password_label),
            style = MaterialTheme.typography.titleSmall,
            color = Grey500
        )

        Spacer(modifier = Modifier.height(9.dp))

        HelpJobTextField(
            value = uiState.password,
            onValueChange = viewModel::updatePassword,
            label = "",
            isPassword = true,
            isError = uiState.passwordError,
            errorMessage = uiState.passwordErrorMessage?.let { stringResource(id = it) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(46.dp))

        // 로그인 버튼
        HelpJobButton(
            text = stringResource(id = R.string.sign_in_button),
            onClick = viewModel::signIn,
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