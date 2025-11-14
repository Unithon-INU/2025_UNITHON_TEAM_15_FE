package unithon.helpjob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat

/**
 * Android 진입점 Activity
 * - EdgeToEdge 설정
 * - 상태바 아이콘 색상 설정
 * - App() 호출 (플랫폼별 래퍼)
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
}