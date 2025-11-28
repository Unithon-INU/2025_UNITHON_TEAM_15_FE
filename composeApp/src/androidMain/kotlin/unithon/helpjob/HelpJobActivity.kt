package unithon.helpjob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import unithon.helpjob.data.repository.GlobalLanguageState
import java.util.Locale

/**
 * Android 진입점 Activity
 * - EdgeToEdge 설정
 * - 상태바 아이콘 색상 설정
 * - App() 호출 (플랫폼별 래퍼)
 * - onResume()에서 언어 설정 복원 (OSS Activity 등에서 복귀 시)
 */
class HelpJobActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 상태바 아이콘을 항상 검정색으로 고정
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true  // 검정 아이콘 (라이트 모드)
        }
        setContent {
            App()  // Android 특화 래퍼 → commonMain HelpJobApp() 호출
        }
    }

    override fun onResume() {
        super.onResume()

        // OSS Activity 복귀 시 앱 언어 설정 강제 복원
        // GlobalLanguageState를 단일 진실 공급원(SSOT)으로 사용
        // 양방향 해결: 시스템(한국어)↔앱(영어), 시스템(영어)↔앱(한국어)
        val currentLanguage = GlobalLanguageState.currentLanguage.value
        Locale.setDefault(Locale.forLanguageTag(currentLanguage.code))
    }
}