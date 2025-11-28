package unithon.helpjob.ui.document

import androidx.compose.runtime.Composable

/**
 * iOS actual 구현 - no-op (iOS는 시스템 뒤로가기 버튼이 없음)
 * iOS는 스와이프 제스처로 화면을 닫으며, 이는 플랫폼 표준 UX입니다.
 */
@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS에서는 시스템 뒤로가기 버튼이 없으므로 아무 작업도 하지 않음
}
