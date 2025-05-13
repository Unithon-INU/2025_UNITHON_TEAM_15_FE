// ui/theme/Theme.kt
package unithon.helpjob.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun HelpJobTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Primary500,        // 하단 버튼
            secondary = Primary100,      // 중간 선택 버튼
            tertiary = Grey200,         // 하단 버튼 미적용
            background = Grey000,       // 화면 배경
            surface = Grey200,          // 추가 예정
            onPrimary = Grey000,        // 하단 버튼 텍스트
            onSecondary = Primary500,   // 중간 선택 버튼 텍스트
            onTertiary = Grey400,       // 하단 버튼 미적용 텍스트
            onBackground = Grey700,     // 일반 텍스트
            onSurface = Grey400,        // 추가 예정
            error = Warning            // 에러 메시지
        ),
        typography = Typography,
        content = content
    )
}