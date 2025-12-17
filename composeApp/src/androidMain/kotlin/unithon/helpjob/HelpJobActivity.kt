package unithon.helpjob

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import unithon.helpjob.data.repository.GlobalLanguageState
import java.util.Locale

/**
 * Android 진입점 Activity
 * - EdgeToEdge 설정
 * - 시스템 바 스타일 설정 (라이트 모드 고정)
 * - App() 호출 (플랫폼별 래퍼)
 * - onResume()에서 언어 설정 복원 (OSS Activity 등에서 복귀 시)
 */
class HelpJobActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 시스템 바(상태바, 네비게이션 바)를 라이트 모드로 고정
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,  // Light 모드 색상
                Color.TRANSPARENT   // Dark 모드 색상 (앱이 라이트 모드 고정이므로 동일)
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,  // Light 모드 색상
                Color.TRANSPARENT   // Dark 모드 색상 (앱이 라이트 모드 고정이므로 동일)
            )
        )
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