package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.data.model.WorkDay
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey400
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
    workDayValue: List<WorkDay>, // 🆕 WorkDay enum 리스트 사용
    onWorkDayValueChange: (WorkDay) -> Unit, // 🆕 WorkDay enum 콜백
    workStartTimeValue: String,
    onWorkStartTimeValueChange: (String) -> Unit,
    workEndTimeValue: String,
    onWorkEndTimeValueChange: (String) -> Unit,
    enabled: Boolean,
    onNext: () -> Unit
){
    val context = LocalContext.current

    DocumentInfoScreen(
        modifier = modifier,
        step = step,
        title = title,
        enabled = enabled,
        onNext = onNext
    ) {
        // 🆕 WorkDay enum의 companion object 사용
        val day1List = WorkDay.firstRowDays
        val day2List = WorkDay.secondRowDays + null // 빈 공간을 위해 null 추가

        val timeList = (0..23).flatMap { hour ->
            listOf("00", "30").map { minute ->
                String.format("%02d:%s", hour, minute)
            }
        }

        Column {
            Column {
                Text(
                    text = stringResource(R.string.document_workplace_info_4_work_start_day_list_label),
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey500,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    // 첫 번째 줄 (월~목)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        day1List.forEach { workDay ->
                            WorkDayCard(
                                modifier = Modifier.weight(1f),
                                workDay = workDay,
                                isSelected = workDayValue.contains(workDay),
                                onClick = { onWorkDayValueChange(workDay) },
                                context = context
                            )
                        }
                    }
                    Spacer(Modifier.height(9.dp))
                    // 두 번째 줄 (금~일 + 빈 공간)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        day2List.forEach { workDay ->
                            if (workDay != null) {
                                WorkDayCard(
                                    modifier = Modifier.weight(1f),
                                    workDay = workDay,
                                    isSelected = workDayValue.contains(workDay),
                                    onClick = { onWorkDayValueChange(workDay) },
                                    context = context
                                )
                            } else {
                                // 빈 공간
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(29.dp))
            HelpJobDropdown(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.document_workplace_info_4_work_start_time_label),
                selectedItem = workStartTimeValue,
                items = timeList,
                onItemSelected = {onWorkStartTimeValueChange(it)},
                itemToString = {it},
                placeholder = stringResource(R.string.document_workplace_info_4_work_start_time_placeholder),
                isUpward = true
            )
            Spacer(Modifier.height(29.dp))
            HelpJobDropdown(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.document_workplace_info_4_work_end_time_label),
                selectedItem = workEndTimeValue,
                items = timeList,
                onItemSelected = {onWorkEndTimeValueChange(it)},
                itemToString = {it},
                placeholder = stringResource(R.string.document_workplace_info_4_work_end_time_placeholder),
                isUpward = true
            )
        }
    }
}

// 🆕 재사용 가능한 WorkDay 카드 컴포넌트
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
            contentColor = if (isSelected) Grey600 else Grey400,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 13.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = workDay.getDisplayName(context), // 🆕 현재 언어에 맞는 요일 표시
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun WorkplaceInfo4Preview(){
    HelpJobTheme {
        WorkplaceInfo4Screen (
            modifier = Modifier.fillMaxSize(),
            step = 2,
            title = "취업 예정 근무처 정보를\n입력해주세요",
            enabled = false,
            onNext = {},
            workDayValue = listOf(WorkDay.MONDAY, WorkDay.FRIDAY), // 🆕 enum 사용
            onWorkDayValueChange = {}, // 🆕 enum 콜백
            workStartTimeValue = "",
            onWorkStartTimeValueChange = {},
            workEndTimeValue = "",
            onWorkEndTimeValueChange = {},
        )
    }
}