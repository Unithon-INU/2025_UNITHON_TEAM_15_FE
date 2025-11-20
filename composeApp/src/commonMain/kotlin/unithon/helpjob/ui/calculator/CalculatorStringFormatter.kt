package unithon.helpjob.ui.calculator

import androidx.compose.runtime.Composable

/**
 * 플랫폼별 문자열 포맷팅 함수
 * - Android: 네이티브 stringResource의 포맷 인자 사용 (%d, %1$d 등)
 * - iOS: 수동으로 문자열 조합 (Compose Multiplatform의 제한사항)
 */

@Composable
expect fun formatWorkTime(time: Float): String

@Composable
expect fun formatWorkDays(days: Int): String
