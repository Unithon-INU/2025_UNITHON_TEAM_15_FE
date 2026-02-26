package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_workplace_info_3_hourly_wage_label
import helpjob.composeapp.generated.resources.document_workplace_info_3_hourly_wage_placeholder
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_end_day_label
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_end_day_placeholder
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_end_day_trailing_text
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_end_month_placeholder
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_end_month_trailing_text
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_end_year_placeholder
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_end_year_trailing_text
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_start_date_label
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_start_day_placeholder
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_start_day_trailing_text
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_start_month_placeholder
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_start_month_trailing_text
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_start_year_placeholder
import helpjob.composeapp.generated.resources.document_workplace_info_3_work_start_year_trailing_text
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.document.components.DocumentWageTextField
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.HelpJobTheme
import unithon.helpjob.ui.theme.Warning
import helpjob.composeapp.generated.resources.error_work_date_order
import helpjob.composeapp.generated.resources.error_work_period_over_one_year

/**
 * 윤년 판별 함수
 */
private fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}

/**
 * 특정 년도와 월의 일 수 계산
 */
private fun getDaysInMonth(year: Int, month: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 31 // 기본값
    }
}

/**
 * 1부터 지정된 일 수까지의 문자열 리스트 생성
 */
private fun createDayList(maxDay: Int): List<String> {
    return (1..maxDay).map { it.toString() }
}

@Composable
fun WorkplaceInfo3Screen(
    modifier: Modifier = Modifier,
    step: Int,
    title: String,
    hourlyWageValue: String,
    onHourlyWageValueChange: (String) -> Unit,
    workStartYearValue: String,
    onWorkStartYearValueChange: (String) -> Unit,
    workStartMonthValue: String,
    onWorkStartMonthValueChange: (String) -> Unit,
    workStartDayValue: String,
    onWorkStartDayValueChange: (String) -> Unit,
    workEndYearValue: String,
    onWorkEndYearValueChange: (String) -> Unit,
    workEndMonthValue: String,
    onWorkEndMonthValueChange: (String) -> Unit,
    workEndDayValue: String,
    onWorkEndDayValueChange: (String) -> Unit,
    isHourlyWageBelowMinimum: Boolean,
    isDateOrderError: Boolean,
    isPeriodOverOneYear: Boolean,
    enabled: Boolean,
    onNext: () -> Unit
){
    val yearList = listOf(
        "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035"
    )

    val monthList = listOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
    )

    // 시작일 - 동적 일 수 계산
    val startDayList = remember(workStartYearValue, workStartMonthValue) {
        derivedStateOf {
            val year = workStartYearValue.toIntOrNull() ?: 2025
            val month = workStartMonthValue.toIntOrNull() ?: 1
            val maxDays = getDaysInMonth(year, month)
            createDayList(maxDays)
        }
    }.value

    // 종료일 - 동적 일 수 계산
    val endDayList = remember(workEndYearValue, workEndMonthValue) {
        derivedStateOf {
            val year = workEndYearValue.toIntOrNull() ?: 2025
            val month = workEndMonthValue.toIntOrNull() ?: 1
            val maxDays = getDaysInMonth(year, month)
            createDayList(maxDays)
        }
    }.value

    // 시작일이 현재 월의 최대 일 수를 초과하면 자동 조정
    LaunchedEffect(workStartYearValue, workStartMonthValue, workStartDayValue) {
        val year = workStartYearValue.toIntOrNull() ?: return@LaunchedEffect
        val month = workStartMonthValue.toIntOrNull() ?: return@LaunchedEffect
        val day = workStartDayValue.toIntOrNull() ?: return@LaunchedEffect

        val maxDays = getDaysInMonth(year, month)
        if (day > maxDays) {
            onWorkStartDayValueChange(maxDays.toString())
        }
    }

    // 종료일이 현재 월의 최대 일 수를 초과하면 자동 조정
    LaunchedEffect(workEndYearValue, workEndMonthValue, workEndDayValue) {
        val year = workEndYearValue.toIntOrNull() ?: return@LaunchedEffect
        val month = workEndMonthValue.toIntOrNull() ?: return@LaunchedEffect
        val day = workEndDayValue.toIntOrNull() ?: return@LaunchedEffect

        val maxDays = getDaysInMonth(year, month)
        if (day > maxDays) {
            onWorkEndDayValueChange(maxDays.toString())
        }
    }

    DocumentInfoScreen(
        modifier = modifier,
        step = step,
        title = title,
        enabled = enabled,
        onNext = onNext
    ) {
        Column {
            // 시급 입력
            DocumentWageTextField(
                value = hourlyWageValue,
                onValueChange = onHourlyWageValueChange,
                labelText = stringResource(Res.string.document_workplace_info_3_hourly_wage_label),
                placeholderText = stringResource(Res.string.document_workplace_info_3_hourly_wage_placeholder),
                imeAction = ImeAction.Done
            )

            if (isHourlyWageBelowMinimum) {
                Text(
                    text = stringResource(Res.string.warning_hourly_wage_below_minimum),
                    color = Warning,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(27.dp))

            // 근무 시작일
            Column {
                Text(
                    text = stringResource(Res.string.document_workplace_info_3_work_start_date_label),
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey600,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row {
                    // 시작 년도
                    HelpJobDropdown(
                        modifier = Modifier.weight(1f),
                        label = "",
                        selectedItem = workStartYearValue,
                        items = yearList,
                        onItemSelected = { year ->
                            onWorkStartYearValueChange(year)
                            // 년도가 변경되면 일 수 재계산을 위해 LaunchedEffect가 동작함
                        },
                        itemToString = { it },
                        placeholder = stringResource(Res.string.document_workplace_info_3_work_start_year_placeholder),
                        trailingText = stringResource(Res.string.document_workplace_info_3_work_start_year_trailing_text),
                    )

                    Spacer(Modifier.width(13.dp))

                    // 시작 월
                    HelpJobDropdown(
                        modifier = Modifier.weight(1f),
                        label = "",
                        selectedItem = workStartMonthValue,
                        items = monthList,
                        onItemSelected = { month ->
                            onWorkStartMonthValueChange(month)
                            // 월이 변경되면 일 수 재계산을 위해 LaunchedEffect가 동작함
                        },
                        itemToString = { it },
                        placeholder = stringResource(Res.string.document_workplace_info_3_work_start_month_placeholder),
                        trailingText = stringResource(Res.string.document_workplace_info_3_work_start_month_trailing_text),
                    )

                    Spacer(Modifier.width(13.dp))

                    // 시작 일 (동적으로 계산된 일 수)
                    HelpJobDropdown(
                        modifier = Modifier.weight(1f),
                        label = "",
                        selectedItem = workStartDayValue,
                        items = startDayList,
                        onItemSelected = { day ->
                            onWorkStartDayValueChange(day)
                        },
                        itemToString = { it },
                        placeholder = stringResource(Res.string.document_workplace_info_3_work_start_day_placeholder),
                        trailingText = stringResource(Res.string.document_workplace_info_3_work_start_day_trailing_text),
                    )
                }
            }

            Spacer(Modifier.height(27.dp))

            // 근무 종료일
            Column {
                Text(
                    text = stringResource(Res.string.document_workplace_info_3_work_end_day_label),
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey600,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row {
                    // 종료 년도
                    HelpJobDropdown(
                        modifier = Modifier.weight(1f),
                        label = "",
                        selectedItem = workEndYearValue,
                        items = yearList,
                        onItemSelected = { year ->
                            onWorkEndYearValueChange(year)
                            // 년도가 변경되면 일 수 재계산을 위해 LaunchedEffect가 동작함
                        },
                        itemToString = { it },
                        placeholder = stringResource(Res.string.document_workplace_info_3_work_end_year_placeholder),
                        trailingText = stringResource(Res.string.document_workplace_info_3_work_end_year_trailing_text),
                    )

                    Spacer(Modifier.width(13.dp))

                    // 종료 월
                    HelpJobDropdown(
                        modifier = Modifier.weight(1f),
                        label = "",
                        selectedItem = workEndMonthValue,
                        items = monthList,
                        onItemSelected = { month ->
                            onWorkEndMonthValueChange(month)
                            // 월이 변경되면 일 수 재계산을 위해 LaunchedEffect가 동작함
                        },
                        itemToString = { it },
                        placeholder = stringResource(Res.string.document_workplace_info_3_work_end_month_placeholder),
                        trailingText = stringResource(Res.string.document_workplace_info_3_work_end_month_trailing_text),
                    )

                    Spacer(Modifier.width(13.dp))

                    // 종료 일 (동적으로 계산된 일 수)
                    HelpJobDropdown(
                        modifier = Modifier.weight(1f),
                        label = "",
                        selectedItem = workEndDayValue,
                        items = endDayList,
                        onItemSelected = { day ->
                            onWorkEndDayValueChange(day)
                        },
                        itemToString = { it },
                        placeholder = stringResource(Res.string.document_workplace_info_3_work_end_day_placeholder),
                        trailingText = stringResource(Res.string.document_workplace_info_3_work_end_day_trailing_text),
                    )
                }

                if (isDateOrderError) {
                    Text(
                        text = stringResource(Res.string.error_work_date_order),
                        color = Warning,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else if (isPeriodOverOneYear) {
                    Text(
                        text = stringResource(Res.string.error_work_period_over_one_year),
                        color = Warning,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

// Preview functions moved to androidMain for Android-only preview support