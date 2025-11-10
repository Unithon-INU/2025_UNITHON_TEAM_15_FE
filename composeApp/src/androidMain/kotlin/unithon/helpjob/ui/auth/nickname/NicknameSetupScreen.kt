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
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.desc
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.resources.MR
import unithon.helpjob.data.repository.LanguageAwareScreen
import unithon.helpjob.ui.auth.components.AuthNicknameTextField
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.HelpJobTheme

@Composable
fun NicknameSetupScreen(
    onNicknameSet: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    viewModel: NicknameSetupViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.collect { messageRes ->
            snackbarHostState.showSnackbar(
                message = messageRes.desc().toString(context)
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
 * NicknameSetup UI 컨텐츠 (프리뷰 지원을 위해 분리)
 */
@Composable
private fun NicknameSetupScreenContent(
    uiState: NicknameSetupViewModel.NicknameSetupUiState,
    onNicknameChange: (String) -> Unit,
    onCompleteClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LanguageAwareScreen {
        Column(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            HelpJobTopAppBar(
                title = MR.strings.nickname_setup_top_bar_title,
                onBack = onBack
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 19.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
            ) {
                // 제목
                Text(
                    text = stringResource(MR.strings.nickname_setup_title),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Grey700
                )

                Spacer(modifier = Modifier.height(39.dp))

                AuthNicknameTextField(
                    value = uiState.nickname,
                    onValueChange = onNicknameChange,
                    placeholderText = stringResource(MR.strings.nickname_placeholder),
                    isError = uiState.nicknameError,
                    errorMessage = uiState.nicknameErrorMessage?.let { stringResource(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                // 완료 버튼
                HelpJobButton(
                    text = stringResource(MR.strings.nickname_complete_button),
                    onClick = onCompleteClick,
                    enabled = uiState.isInputValid,
                    isLoading = uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// =================================
// 프리뷰들
// =================================

// 기본 상태 프리뷰
@Preview(
    name = "기본 상태",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun NicknameSetupScreenPreview() {
    HelpJobTheme {
        NicknameSetupScreenContent(
            uiState = NicknameSetupViewModel.NicknameSetupUiState(
                nickname = "",
                nicknameLength = 0
            ),
            onNicknameChange = {},
            onCompleteClick = {},
            onBack = {}
        )
    }
}

// 입력된 상태 프리뷰
@Preview(
    name = "입력된 상태",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun NicknameSetupScreenInputPreview() {
    HelpJobTheme {
        NicknameSetupScreenContent(
            uiState = NicknameSetupViewModel.NicknameSetupUiState(
                nickname = "헬프잡",
                nicknameLength = 3
            ),
            onNicknameChange = {},
            onCompleteClick = {},
            onBack = {}
        )
    }
}

// 에러 상태 프리뷰
@Preview(
    name = "에러 상태",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun NicknameSetupScreenErrorPreview() {
    HelpJobTheme {
        NicknameSetupScreenContent(
            uiState = NicknameSetupViewModel.NicknameSetupUiState(
                nickname = "중복닉네임",
                nicknameLength = 5,
                nicknameError = true,
                nicknameErrorMessage = MR.strings.nickname_duplicate_error
            ),
            onNicknameChange = {},
            onCompleteClick = {},
            onBack = {}
        )
    }
}