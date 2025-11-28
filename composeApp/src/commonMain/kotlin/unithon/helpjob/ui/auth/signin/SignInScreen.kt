package unithon.helpjob.ui.auth.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.sign_in_button
import helpjob.composeapp.generated.resources.sign_in_email_hint
import helpjob.composeapp.generated.resources.sign_in_email_label
import helpjob.composeapp.generated.resources.sign_in_go_to_sign_up
import helpjob.composeapp.generated.resources.sign_in_no_account
import helpjob.composeapp.generated.resources.sign_in_or_divider
import helpjob.composeapp.generated.resources.sign_in_password_hint
import helpjob.composeapp.generated.resources.sign_in_password_label
import helpjob.composeapp.generated.resources.sign_in_welcome_main
import helpjob.composeapp.generated.resources.sign_in_welcome_sub
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.ui.auth.components.AuthEmailTextField
import unithon.helpjob.ui.auth.components.AuthPasswordTextField
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.Primary600
import unithon.helpjob.util.noRippleClickable

@Composable
fun SignInScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    viewModel: SignInViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.collect { messageRes ->
            snackbarHostState.showSnackbar(
                message = getString(messageRes)
            )
        }
    }

    LaunchedEffect(uiState.isSignInSuccessful) {
        if (uiState.isSignInSuccessful) {
            onNavigateToOnboarding()
        }
    }

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

@Composable
internal fun SignInContent(
    uiState: SignInViewModel.SignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.sign_in_welcome_main),
            style = MaterialTheme.typography.headlineLarge,
            color = Grey700
        )

        Text(
            text = stringResource(Res.string.sign_in_welcome_sub),
            style = MaterialTheme.typography.headlineMedium,
            color = Grey700
        )

        Spacer(modifier = Modifier.height(39.dp))

        AuthEmailTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            labelText = stringResource(Res.string.sign_in_email_label),
            placeholderText = stringResource(Res.string.sign_in_email_hint),
            isError = uiState.emailError,
            errorMessage = uiState.emailErrorMessage?.let { stringResource(it) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        AuthPasswordTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            labelText = stringResource(Res.string.sign_in_password_label),
            placeholderText = stringResource(Res.string.sign_in_password_hint),
            isError = uiState.passwordError,
            errorMessage = uiState.passwordErrorMessage?.let { stringResource(it) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        HelpJobButton(
            text = stringResource(Res.string.sign_in_button),
            onClick = onSignInClick,
            enabled = uiState.isInputValid,
            isLoading = uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Grey300)
            )
            Text(
                text = stringResource(Res.string.sign_in_or_divider),
                style = MaterialTheme.typography.bodyLarge,
                color = Grey600,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.sign_in_no_account),
                style = MaterialTheme.typography.bodyLarge,
                color = Grey600
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(Res.string.sign_in_go_to_sign_up),
                style = MaterialTheme.typography.bodyLarge,
                color = Primary600,
                modifier = Modifier.noRippleClickable { onNavigateToSignUp() }
            )
        }
    }
}
