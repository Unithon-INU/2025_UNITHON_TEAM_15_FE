package unithon.helpjob.ui.auth.nickname

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.nickname_complete_button
import helpjob.composeapp.generated.resources.nickname_placeholder
import helpjob.composeapp.generated.resources.nickname_setup_title
import helpjob.composeapp.generated.resources.nickname_setup_top_bar_title
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.ui.auth.components.AuthNicknameTextField
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.theme.Grey700

@Composable
fun NicknameSetupScreen(
    onNicknameSet: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    viewModel: NicknameSetupViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.collect { messageRes ->
            snackbarHostState.showSnackbar(
                message = getString(messageRes)
            )
        }
    }

    // 닉네임 설정 성공시 네비게이션
    LaunchedEffect(uiState.isNicknameSet) {
        if (uiState.isNicknameSet) {
            onNicknameSet()
        }
    }

    NicknameSetupScreenContent(
        uiState = uiState,
        onNicknameChange = viewModel::updateNickname,
        onCompleteClick = viewModel::setNickname,
        onBack = onBack,
        modifier = modifier
    )
}

/**
 * NicknameSetup UI 컨텐츠
 */
@Composable
internal fun NicknameSetupScreenContent(
    uiState: NicknameSetupViewModel.NicknameSetupUiState,
    onNicknameChange: (String) -> Unit,
    onCompleteClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        HelpJobTopAppBar(
            title = Res.string.nickname_setup_top_bar_title,
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 19.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
        ) {
            // 제목
            Text(
                text = stringResource(Res.string.nickname_setup_title),
                style = MaterialTheme.typography.headlineLarge,
                color = Grey700
            )

            Spacer(modifier = Modifier.height(39.dp))

            AuthNicknameTextField(
                value = uiState.nickname,
                onValueChange = onNicknameChange,
                placeholderText = stringResource(Res.string.nickname_placeholder),
                isError = uiState.nicknameError,
                errorMessage = uiState.nicknameErrorMessage?.let { stringResource(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // 완료 버튼
            HelpJobButton(
                text = stringResource(Res.string.nickname_complete_button),
                onClick = onCompleteClick,
                enabled = uiState.isInputValid,
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
