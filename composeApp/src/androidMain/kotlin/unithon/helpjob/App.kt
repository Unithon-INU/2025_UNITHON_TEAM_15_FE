package unithon.helpjob

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import unithon.helpjob.data.repository.DynamicLanguageProvider
import unithon.helpjob.data.repository.GlobalLanguageState
import unithon.helpjob.ui.theme.HelpJobTheme

/**
 * Android 진입점 래퍼
 * - HelpJobTheme (Android 특화 테마)
 * - DynamicLanguageProvider (Android 특화 언어 처리)
 * - HelpJobApp (공통 UI)
 */
@Composable
fun App() {
    val currentLanguage by GlobalLanguageState.currentLanguage

    HelpJobTheme {
        // DynamicLanguageProvider로 LocalContext를 언어별로 재생성
        // 이렇게 해야 stringResource()가 올바른 locale의 텍스트를 가져옴
        DynamicLanguageProvider(currentLanguage = currentLanguage) {
            HelpJobApp()
        }
    }
}
