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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
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
    workDayValue: List<String>,
    onWorkDayValueChange: (String) -> Unit,
    workStartTimeValue: String,
    onWorkStartTimeValueChange: (String) -> Unit,
    workEndTimeValue: String,
    onWorkEndTimeValueChange: (String) -> Unit,
    enabled: Boolean,
    onNext: () -> Unit
){
    DocumentInfoScreen(
        modifier = modifier,
        step = step,
        title = title,
        enabled = enabled,
        onNext = onNext
    ) {
        val day1List = listOf(
            "월", "화", "수", "목"
        )
        val day2List = listOf(
            "금", "토", "일", ""
        )

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
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        day1List.forEach { day ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .noRippleClickable {
                                        onWorkDayValueChange(day)
                                    },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (workDayValue.contains(day)) Primary300 else Grey200,
                                    contentColor = if (workDayValue.contains(day)) Grey600 else Grey400,
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
                                        text = day,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(9.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        day2List.forEach { day ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .noRippleClickable {
                                        onWorkDayValueChange(day)
                                    },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (day.isBlank()) {
                                        Color.Transparent
                                    } else {
                                        if (workDayValue.contains(day)) Primary300  else Grey200
                                    },
                                    contentColor = if (workDayValue.contains(day)) Grey600 else Grey400,
                                )
                            ) {
                                if (day.isNotBlank()){
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 13.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = day,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(29.dp))
            HelpJobDropdown(
                modifier = Modifier
                    .fillMaxWidth(),
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
                modifier = Modifier
                    .fillMaxWidth(),
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun WorkplaceInfo4Preview(

){
    HelpJobTheme {
        WorkplaceInfo4Screen (
            modifier = Modifier
                .fillMaxSize(),
            step = 2,
            title = "취업 예정 근무처 정보를\n입력해주세요",
            enabled = false,
            onNext = {},
            workDayValue = listOf(),
            onWorkDayValueChange = {},
            workStartTimeValue = "",
            onWorkStartTimeValueChange = {},
            workEndTimeValue = "",
            onWorkEndTimeValueChange = {},
        )
    }
}