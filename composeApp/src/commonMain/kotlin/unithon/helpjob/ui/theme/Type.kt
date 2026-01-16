package unithon.helpjob.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.pretendard_bold
import helpjob.composeapp.generated.resources.pretendard_medium
import helpjob.composeapp.generated.resources.pretendard_regular
import org.jetbrains.compose.resources.Font

@Composable
fun getPretendardFontFamily() = FontFamily(
    Font(Res.font.pretendard_regular, FontWeight.Normal),
    Font(Res.font.pretendard_medium, FontWeight.Medium),
    Font(Res.font.pretendard_bold, FontWeight.Bold)
)

@Composable
fun getTypography(): Typography {
    val fontFamily = getPretendardFontFamily()

    return Typography(
        // Headline1 - 24px, Bold
        headlineLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        // Headline2 - 20px, Bold
        headlineMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            letterSpacing = 0.sp
        ),
        // Subhead1 - 16px, Medium
        titleLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
        // Title1 - 16px, Bold
        titleMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
        // Title2 - 14px, Bold
        titleSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            letterSpacing = 0.sp
        ),
        // Body1 - 15px, Bold
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            lineHeight = 19.sp,
            letterSpacing = 0.sp
        ),
        // Body2 - 15px, Medium
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            lineHeight = 19.sp,
            letterSpacing = 0.sp
        ),
        // Body3 - 15px, Regular
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 19.sp,
            letterSpacing = 0.sp
        ),
        // Body4 - 12px, Medium (labelMedium 사용)
        labelMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 15.sp,
            letterSpacing = 0.sp
        )
    )
}
