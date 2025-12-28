package unithon.helpjob.ui.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

/**
 * Android actual 구현 - 시스템 뒤로가기 버튼 처리
 */
@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enabled, onBack = onBack)
}
