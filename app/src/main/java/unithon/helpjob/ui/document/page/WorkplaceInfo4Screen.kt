package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.data.model.WorkDay
import unithon.helpjob.ui.components.HelpJobCheckbox
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.document.DocumentViewModel
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.HelpJobTheme
import unithon.helpjob.ui.theme.Primary300
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
    isAllDaysSelected: Boolean,
    onToggleAllDays: () -> Unit,
    isSameTimeForAll: Boolean,
    onToggleSameTimeForAll: () -> Unit,
    enabled: Boolean,
    onNext: () -> Unit
){
    val context = LocalContext.current
    val timeList = (0..23).flatMap { hour ->
        listOf("00", "30").map { minute ->
            String.format("%02d:%s", hour, minute)
        }
    }
    val scrollState = rememberScrollState()

    DocumentInfoScreen(
        modifier = modifier,
        step = step,
        title = title,
        enabled = enabled,
        contentPadding = 28.dp,
        verticalScrollable = false,
        onNext = onNext
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // 주간 근무 요일 섹션
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.document_workplace_info_4_work_days_label),
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey600
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HelpJobCheckbox(
                        checked = isAllDaysSelected,
                        onCheckedChange = { onToggleAllDays() }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.document_workplace_info_4_everyday),
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
                        onClick = { onWorkDayChange(workDay) },
                        context = context
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
                        text = stringResource(R.string.document_workplace_info_4_work_time_label),
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
                            text = stringResource(R.string.document_workplace_info_4_same_time),
                            style = MaterialTheme.typography.titleSmall,
                            color = Grey600
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                // 선택된 요일들의 시간 입력
                workDays.forEach { workDay ->
                    val dayTime = workDayTimes[workDay] ?: DocumentViewModel.WorkDayTime()

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
                                    text = workDay.getDisplayName(context),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Grey600,
                                    modifier = Modifier.padding(vertical = 13.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // 시작 시간 드롭다운
                        HelpJobDropdown(
                            modifier = Modifier.weight(1f),
                            label = "",
                            selectedItem = dayTime.startTime.ifEmpty { null },
                            items = timeList,
                            onItemSelected = { time ->
                                onWorkDayStartTimeChange(workDay, time)
                            },
                            itemToString = { it },
                            placeholder = stringResource(R.string.document_workplace_info_4_start_time_placeholder),
                            trailingText = stringResource(R.string.document_workplace_info_4_time_unit),
                            isUpward = true,
                            showScrollbar = false
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "~",
                            style = MaterialTheme.typography.titleMedium,
                            color = Grey600
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // 종료 시간 드롭다운
                        HelpJobDropdown(
                            modifier = Modifier.weight(1f),
                            label = "",
                            selectedItem = dayTime.endTime.ifEmpty { null },
                            items = timeList,
                            onItemSelected = { time ->
                                onWorkDayEndTimeChange(workDay, time)
                            },
                            itemToString = { it },
                            placeholder = stringResource(R.string.document_workplace_info_4_end_time_placeholder),
                            trailingText = stringResource(R.string.document_workplace_info_4_time_unit),
                            isUpward = true,
                            showScrollbar = false
                        )
                    }
                    Spacer(Modifier.height(10.dp))
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
    onClick: () -> Unit,
    context: android.content.Context
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
                text = workDay.getDisplayName(context),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ko")
@Composable
fun WorkplaceInfo4Preview(){
    HelpJobTheme {
        WorkplaceInfo4Screen(
            modifier = Modifier.fillMaxSize(),
            step = 2,
            title = "취업 예정 근무처 정보를\n입력해주세요",
            enabled = false,
            onNext = {},
            workDays = listOf(WorkDay.MONDAY),
            onWorkDayChange = {},
            workDayTimes = mapOf(
                WorkDay.MONDAY to DocumentViewModel.WorkDayTime("01:30", "5:00")
            ),
            onWorkDayStartTimeChange = { _, _ -> },
            onWorkDayEndTimeChange = { _, _ -> },
            isAllDaysSelected = false,
            onToggleAllDays = {},
            isSameTimeForAll = false,
            onToggleSameTimeForAll = {}
        )
    }
}