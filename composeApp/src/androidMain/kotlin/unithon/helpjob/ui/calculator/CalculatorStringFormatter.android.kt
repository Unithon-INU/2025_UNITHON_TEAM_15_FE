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
 * Android actual 구현
 * Android의 네이티브 stringResource는 포맷 인자를 자동으로 처리합니다
 */
@Composable
actual fun formatWorkTime(time: Float): String {
    val hours = time.toInt()
    val minutes = ((time - hours) * 60).toInt()

    // Android는 %d, %1$d 같은 포맷 지정자를 자동으로 처리
    return if (minutes == 0) {
        if (hours == 1) {
            stringResource(Res.string.calculator_hours_format_singular, hours)
        } else {
            stringResource(Res.string.calculator_hours_format_plural, hours)
        }
    } else {
        when {
            hours == 1 && minutes == 1 ->
                stringResource(Res.string.calculator_hours_minutes_format_singular_singular, hours, minutes)
            hours == 1 && minutes > 1 ->
                stringResource(Res.string.calculator_hours_minutes_format_singular_plural, hours, minutes)
            hours > 1 && minutes == 1 ->
                stringResource(Res.string.calculator_hours_minutes_format_plural_singular, hours, minutes)
            else ->
                stringResource(Res.string.calculator_hours_minutes_format_plural_plural, hours, minutes)
        }
    }
}

@Composable
actual fun formatWorkDays(days: Int): String {
    // Android는 포맷 인자를 자동으로 처리
    return if (days == 1) {
        stringResource(Res.string.calculator_days_format_singular, days)
    } else {
        stringResource(Res.string.calculator_days_format_plural, days)
    }
}
