package unithon.helpjob.ui.auth.nickname

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.*

@Composable
fun NicknameSetupScreen(
    onNicknameSet: () -> Unit,
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // 제목
        Text(
            text = stringResource(id = R.string.nickname_setup_title),
            style = MaterialTheme.typography.headlineLarge,
            color = Grey700
        )

        Spacer(modifier = Modifier.height(39.dp))

        // 닉네임 입력 필드
        Column {
            HelpJobTextField(
                value = uiState.nickname,
                onValueChange = viewModel::updateNickname,
                label = "",
                placeholder = stringResource(id = R.string.nickname_placeholder),
                isError = uiState.nicknameError,
                errorMessage = uiState.nicknameErrorMessage?.let { stringResource(id = it) },
                modifier = Modifier.fillMaxWidth()
            )

            // 글자 수 카운터
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(
                        id = R.string.nickname_character_count,
                        uiState.nicknameLength
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = Grey400,
                    textAlign = TextAlign.End
                )
            }
        }

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