package unithon.helpjob.ui.auth.nickname

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.data.repository.LanguageAwareScreen
import unithon.helpjob.ui.auth.components.AuthNicknameTextField
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.theme.Grey700

@Composable
fun NicknameSetupScreen(
    onNicknameSet: () -> Unit,
    onBack: () -> Unit, // 🆕 뒤로가기 추가
    modifier: Modifier = Modifier,
    viewModel: NicknameSetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 닉네임 설정 성공시 네비게이션
    LaunchedEffect(uiState.isNicknameSet) {
        if (uiState.isNicknameSet) {
            onNicknameSet()
        }
    }

    LanguageAwareScreen {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                HelpJobTopAppBar(
                    title = R.string.nickname_setup_top_bar_title,
                    onBack = onBack
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(top = 19.dp, start = 20.dp, end = 20.dp),
            ) {
                // 제목
                Text(
                    text = stringResource(id = R.string.nickname_setup_title),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Grey700
                )

                Spacer(modifier = Modifier.height(39.dp))

                AuthNicknameTextField(
                    value = uiState.nickname,
                    onValueChange = viewModel::updateNickname,
                    placeholderText = stringResource(id = R.string.nickname_placeholder),
                    isError = uiState.nicknameError,
                    errorMessage = uiState.nicknameErrorMessage?.let { stringResource(id = it) },
                        modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.weight(1f))

                // 완료 버튼
                HelpJobButton(
                    text = stringResource(id = R.string.nickname_complete_button),
                    onClick = viewModel::setNickname,
                    enabled = uiState.isInputValid,
                    isLoading = uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
            }
        }
    }
}