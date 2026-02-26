package unithon.helpjob

import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIViewController
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.data.repository.GlobalLanguageState

/**
 * iOS용 메인 ViewController
 * SwiftUI ContentView에서 호출됨
 */
fun MainViewController(): UIViewController {
    // iOS 언어 초기화: App.kt의 DisposableEffect가 저장한 값 동기 읽기
    // DataStore(비동기)보다 먼저 실행되어 Compose 렌더링 전에 올바른 언어 설정
    try {
        @Suppress("UNCHECKED_CAST")
        val savedLanguages = NSUserDefaults.standardUserDefaults
            .stringArrayForKey("AppleLanguages") as? List<String>
        val savedLanguageCode = savedLanguages?.firstOrNull()
        if (!savedLanguageCode.isNullOrBlank()) {
            GlobalLanguageState.initializeLanguage(AppLanguage.fromCode(savedLanguageCode))
        }
        // null이면 mutableStateOf(ENGLISH) 기본값 유지 (첫 실행 시)
    } catch (e: Exception) {
        // 초기화 실패 시 기본값(ENGLISH) 유지
    }

    return ComposeUIViewController { App() }
}
