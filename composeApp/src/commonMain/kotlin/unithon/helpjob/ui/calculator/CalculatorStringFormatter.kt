package unithon.helpjob.ui.calculator

import androidx.compose.runtime.Composable
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.calculator_days_format_plural
import helpjob.composeapp.generated.resources.calculator_days_format_singular
import helpjob.composeapp.generated.resources.calculator_hours_format_plural
import helpjob.composeapp.generated.resources.calculator_hours_format_singular
import helpjob.composeapp.generated.resources.calculator_hours_minutes_format_plural_plural
import helpjob.composeapp.generated.resources.calculator_hours_minutes_format_plural_singular
import helpjob.composeapp.generated.resources.calculator_hours_minutes_format_singular_plural
import helpjob.composeapp.generated.resources.calculator_hours_minutes_format_singular_singular
import org.jetbrains.compose.resources.stringResource

/**
 * 계산기 화면 전용 문자열 포맷팅 함수
 *
 * Compose Multiplatform Resources의 stringResource()는
 * 포맷 인자(%d, %1$d 등)를 자동으로 처리하지 못하므로
 * strings.xml에서 템플릿을 가져온 후 수동으로 replace()를 사용하여 포맷팅합니다.
 */

@Composable
fun formatWorkTime(time: Float): String {
    val hours = time.toInt()
    val minutes = ((time - hours) * 60).toInt()

    return if (minutes == 0) {
        val template = if (hours == 1) {
            stringResource(Res.string.calculator_hours_format_singular)
        } else {
            stringResource(Res.string.calculator_hours_format_plural)
        }
        // "%d hour" → "5 hour"
        template.replace("%d", hours.toString())
    } else {
        val template = when {
            hours == 1 && minutes == 1 ->
                stringResource(Res.string.calculator_hours_minutes_format_singular_singular)
            hours == 1 && minutes > 1 ->
                stringResource(Res.string.calculator_hours_minutes_format_singular_plural)
            hours > 1 && minutes == 1 ->
                stringResource(Res.string.calculator_hours_minutes_format_plural_singular)
            else ->
                stringResource(Res.string.calculator_hours_minutes_format_plural_plural)
        }
        // "%1$d hours %2$d minutes" → "5 hours 30 minutes"
        template
            .replace("%1\$d", hours.toString())
            .replace("%2\$d", minutes.toString())
    }
}

@Composable
fun formatWorkDays(days: Int): String {
    val template = if (days == 1) {
        stringResource(Res.string.calculator_days_format_singular)
    } else {
        stringResource(Res.string.calculator_days_format_plural)
    }
    // "%d day" → "3 days"
    return template.replace("%d", days.toString())
}
