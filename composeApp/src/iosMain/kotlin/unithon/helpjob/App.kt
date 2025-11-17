package unithon.helpjob

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import platform.Foundation.NSBundle
import platform.Foundation.NSUserDefaults
import unithon.helpjob.data.repository.GlobalLanguageState
import unithon.helpjob.ui.theme.HelpJobTheme

/**
 * iOS 진입점 래퍼
 * - HelpJobTheme (공통 테마)
 * - 언어 상태 관리 (iOS 특화)
 * - HelpJobApp (공통 UI)
 */
@Composable
fun App() {
    val currentLanguage by GlobalLanguageState.currentLanguage

    // 🔥 언어 변경 시 NSUserDefaults에 설정하여 시스템 로케일 변경
    DisposableEffect(currentLanguage.code) {
        val defaults = NSUserDefaults.standardUserDefaults
        defaults.setObject(listOf(currentLanguage.code), forKey = "AppleLanguages")
        defaults.synchronize()

        println("✅ [iOS] AppleLanguages 설정: ${currentLanguage.code}")

        onDispose { }
    }

    HelpJobTheme {
        // 🔥 언어가 변경될 때마다 전체 UI를 재구성
        androidx.compose.runtime.key(currentLanguage.code) {
            HelpJobApp()
        }
    }
}
