package unithon.helpjob.ui.document.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_work_hours_format_hours
import helpjob.composeapp.generated.resources.document_work_hours_format_hours_minutes
import helpjob.composeapp.generated.resources.document_work_hours_format_minutes
import helpjob.composeapp.generated.resources.document_work_hours_combined_overtime_warning
import helpjob.composeapp.generated.resources.document_work_hours_weekday_overtime_weekend_unlimited
import helpjob.composeapp.generated.resources.document_work_hours_weekday
import helpjob.composeapp.generated.resources.document_work_hours_weekend
import helpjob.composeapp.generated.resources.document_work_hours_vacation_info
import helpjob.composeapp.generated.resources.document_workplace_info_4_vacation
import helpjob.composeapp.generated.resources.document_workplace_info_4_same_time
import helpjob.composeapp.generated.resources.document_workplace_info_4_work_days_label
import helpjob.composeapp.generated.resources.document_workplace_info_4_work_time_label
import helpjob.composeapp.generated.resources.exclamation_mark
import androidx.compose.ui.text.input.ImeAction
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.data.model.WorkDay
import unithon.helpjob.ui.components.HelpJobCheckbox
import unithon.helpjob.ui.document.DocumentViewModel
import unithon.helpjob.ui.document.components.DocumentTimeTextField
import unithon.helpjob.ui.theme.Grey400
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Primary300
import unithon.helpjob.ui.theme.Warning
import unithon.helpjob.ui.theme.body4
import unithon.helpjob.ui.theme.title1
import unithon.helpjob.util.noRippleClickable

@Composable
fun WorkplaceInfo4Screen(
    modifier: Modifier = Modifier,
    step: Int,
    title: String,
    workDays: List<WorkDay>,
    onWorkDayChange: (WorkDay) -> Unit,
    workDayTimes: Map<WorkDay, DocumentViewModel.WorkDayTime>,
    onWorkDayStartTimeChange: (WorkDay, String) -> Unit,
    onWorkDayEndTimeChange: (WorkDay, String) -> Unit,
    isVacation: Boolean,
    onToggleVacation: () -> Unit,
    isSameTimeForAll: Boolean,
    onToggleSameTimeForAll: () -> Unit,
    weekdayTotalHours: Float,
    weekendTotalHours: Float,
    isWeekdayOvertime: Boolean,
    isWeekendOvertime: Boolean,
    weeklyHoursLimit: Int? = null,
    maxWeekdayHours: Float? = null,
    isWeekendUnlimited: Boolean = false,
    enabled: Boolean,
    onNext: () -> Unit
){
    val scrollState = rememberScrollState()
    val hasWorkHours = weekdayTotalHours > 0 || weekendTotalHours > 0

    DocumentInfoScreen(
        modifier = modifier,
        step = step,
        title = title,
        enabled = enabled,
        contentPadding = if (hasWorkHours) 9.dp else 28.dp,
        verticalScrollable = false,
        onNext = onNext
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // [고정 영역] 주중/주말 시간 요약 - 스크롤 밖에 배치
            if (hasWorkHours) {
                WorkHoursSummary(
                    weekdayTotalHours = weekdayTotalHours,
                    weekendTotalHours = weekendTotalHours,
                    isWeekdayOvertime = isWeekdayOvertime,
                    isWeekendOvertime = isWeekendOvertime,
                    isVacation = isVacation,
                    weeklyHoursLimit = weeklyHoursLimit,
                    maxWeekdayHours = maxWeekdayHours,
                    isWeekendUnlimited = isWeekendUnlimited
                )
                Spacer(modifier = Modifier.height(28.dp))
            }

            // [스크롤 영역] 요일 선택 + 시간 입력
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
            // 주간 근무 요일 섹션
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.document_workplace_info_4_work_days_label),
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey600
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HelpJobCheckbox(
                        checked = isVacation,
                        onCheckedChange = { onToggleVacation() }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.document_workplace_info_4_vacation),
                        style = MaterialTheme.typography.titleSmall,
                        color = Grey600
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 요일 선택 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WorkDay.entries.forEach { workDay ->
                    WorkDayCard(
                        modifier = Modifier.weight(1f),
                        workDay = workDay,
                        isSelected = workDays.contains(workDay),
                        onClick = { onWorkDayChange(workDay) }
                    )
                }
            }

            // 선택된 요일이 있을 때만 시간 입력 섹션 표시
            if (workDays.isNotEmpty()) {
                Spacer(modifier = Modifier.height(29.dp))

                // 시작/종료 시간 섹션
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.document_workplace_info_4_work_time_label),
                        style = MaterialTheme.typography.titleSmall,
                        color = Grey600
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HelpJobCheckbox(
                            checked = isSameTimeForAll,
                            onCheckedChange = { onToggleSameTimeForAll() }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(Res.string.document_workplace_info_4_same_time),
                            style = MaterialTheme.typography.titleSmall,
                            color = Grey600
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                // 선택된 요일들의 시간 입력
                workDays.forEach { workDay ->
                    key(workDay) {
                        val dayTime = workDayTimes[workDay] ?: DocumentViewModel.WorkDayTime()
                        val endTimeFocusRequester = remember { FocusRequester() }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 요일 표시
                            Card(
                                modifier = Modifier.width(40.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Primary300
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = workDay.getDisplayName(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Grey600,
                                        modifier = Modifier.padding(vertical = 13.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // 시작 시간 텍스트필드
                            DocumentTimeTextField(
                                value = dayTime.startTime.replace(":", ""),
                                onValueChange = { raw ->
                                    val formatted = if (raw.length > 2) {
                                        "${raw.substring(0, 2)}:${raw.substring(2)}"
                                    } else if (raw.isNotEmpty()) {
                                        raw
                                    } else ""
                                    onWorkDayStartTimeChange(workDay, formatted)
                                },
                                hourPlaceholder = "예) 13",
                                minutePlaceholder = "30",
                                modifier = Modifier.weight(1f),
                                nextFocusRequester = endTimeFocusRequester
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // 시작-종료 구분자 (horizontal divider)
                            Box(
                                modifier = Modifier
                                    .width(9.dp)
                                    .height(2.dp)
                                    .background(Grey400)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // 종료 시간 텍스트필드
                            DocumentTimeTextField(
                                value = dayTime.endTime.replace(":", ""),
                                onValueChange = { raw ->
                                    val formatted = if (raw.length > 2) {
                                        "${raw.substring(0, 2)}:${raw.substring(2)}"
                                    } else if (raw.isNotEmpty()) {
                                        raw
                                    } else ""
                                    onWorkDayEndTimeChange(workDay, formatted)
                                },
                                hourPlaceholder = "예) 16",
                                minutePlaceholder = "30",
                                modifier = Modifier.weight(1f),
                                focusRequester = endTimeFocusRequester,
                                imeAction = ImeAction.Done
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
            }
        }
    }
}

// 재사용 가능한 WorkDay 카드 컴포넌트
@Composable
private fun WorkDayCard(
    modifier: Modifier = Modifier,
    workDay: WorkDay,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.noRippleClickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Primary300 else Grey200,
            contentColor = if (isSelected) Grey600 else Grey500,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = workDay.getDisplayName(),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

// 주중/주말 근무 시간 요약 컴포넌트
@Composable
private fun WorkHoursSummary(
    weekdayTotalHours: Float,
    weekendTotalHours: Float,
    isWeekdayOvertime: Boolean,
    isWeekendOvertime: Boolean,
    isVacation: Boolean,
    weeklyHoursLimit: Int? = null,
    maxWeekdayHours: Float? = null,
    isWeekendUnlimited: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 주중/주말 시간 (가로 배치)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (weekdayTotalHours > 0) {
                if (!isVacation && isWeekdayOvertime) {
                    Icon(
                        painter = painterResource(Res.drawable.exclamation_mark),
                        contentDescription = null,
                        tint = Warning
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = stringResource(Res.string.document_work_hours_weekday, formatHours(weekdayTotalHours)),
                    style = MaterialTheme.typography.title1,
                    color = if (!isVacation && isWeekdayOvertime) Warning else Grey600
                )
            }

            if (weekdayTotalHours > 0 && weekendTotalHours > 0) {
                Spacer(modifier = Modifier.width(16.dp))
            }

            if (weekendTotalHours > 0) {
                // 주말 무제한이면 ⚠️ 아이콘 숨김
                if (!isVacation && isWeekendOvertime && !isWeekendUnlimited) {
                    Icon(
                        painter = painterResource(Res.drawable.exclamation_mark),
                        contentDescription = null,
                        tint = Warning
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = stringResource(Res.string.document_work_hours_weekend, formatHours(weekendTotalHours)),
                    style = MaterialTheme.typography.title1,
                    color = if (!isVacation && isWeekendOvertime && !isWeekendUnlimited) Warning else Grey600
                )
            }
        }

        // 하단 문구: 방학 안내 또는 초과 경고
        if (isVacation) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(Res.string.document_work_hours_vacation_info),
                style = MaterialTheme.typography.body4,
                color = Grey600
            )
        } else if (weeklyHoursLimit != null && (isWeekdayOvertime || isWeekendOvertime)) {
            // 합산 모드 초과
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(Res.string.document_work_hours_combined_overtime_warning, weeklyHoursLimit),
                style = MaterialTheme.typography.body4,
                color = Warning
            )
        } else if (isWeekdayOvertime && isWeekendUnlimited) {
            // weekdayHours 모드: 주중만 초과 + 주말 무제한
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    Res.string.document_work_hours_weekday_overtime_weekend_unlimited,
                    maxWeekdayHours?.toInt() ?: 0
                ),
                style = MaterialTheme.typography.body4,
                color = Warning
            )
        }
    }
}

// 시간 포맷팅 헬퍼 함수 (KMP 호환, 다국어 지원)
@Composable
private fun formatHours(hours: Float): String {
    val totalMinutes = (hours * 60).toInt()
    val h = totalMinutes / 60
    val m = totalMinutes % 60
    return when {
        h > 0 && m > 0 -> stringResource(Res.string.document_work_hours_format_hours_minutes, h, m)
        h > 0 -> stringResource(Res.string.document_work_hours_format_hours, h)
        else -> stringResource(Res.string.document_work_hours_format_minutes, m)
    }
}

// Preview functions moved to androidMain for Android-only preview support