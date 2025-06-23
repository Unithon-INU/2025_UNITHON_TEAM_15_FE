package unithon.helpjob.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.body4

@Composable
fun LanguageSettingScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LanguageSettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            HelpJobTopAppBar(
                title = R.string.setting_app_language,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(top = 14.dp, start = 20.dp, end = 20.dp)
        ) {
            // 현재 언어 섹션 헤더
            Text(
                text = stringResource(R.string.language_setting_current_language),
                style = MaterialTheme.typography.body4,
                color = Grey700
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 언어 선택 드롭다운 (기존 컴포넌트 재사용)
            HelpJobDropdown(
                items = uiState.availableLanguages,
                selectedItem = uiState.currentLanguage,
                onItemSelected = viewModel::setLanguage,
                itemToString = { it.displayName }
            )
        }
    }
}