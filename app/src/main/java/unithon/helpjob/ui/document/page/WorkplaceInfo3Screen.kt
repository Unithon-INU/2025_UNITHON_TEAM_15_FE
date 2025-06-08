package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.HelpJobTheme
import unithon.helpjob.util.CurrencyVisualTransformation

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
    enabled: Boolean,
    onNext: () -> Unit
){
    val yearList = listOf(
        "2022",
        "2023",
        "2024",
        "2025",
        "2026",
        "2027",
        "2028",
        "2029",
        "2030",
    )
    val monthList = listOf(
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12",
    )
    val dayList = listOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
        "31"
    )

    DocumentInfoScreen(
        modifier = modifier,
        step = step,
        title = title,
        enabled = enabled,
        onNext = onNext
    ) {
        Column {

            HelpJobTextField(
                value = hourlyWageValue,
                onValueChange = onHourlyWageValueChange,
                label = stringResource(R.string.document_workplace_info_3_hourly_wage_label),
                placeholder = stringResource(R.string.document_workplace_info_3_hourly_wage_placeholder),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
                isWon = true,
                visualTransformation = CurrencyVisualTransformation()
            )
            Spacer(Modifier.height(27.dp))
            Column {
                Text(
                    text = stringResource(R.string.document_workplace_info_3_work_start_date_label),
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey500,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row {
                    HelpJobDropdown(
                        modifier = Modifier
                            .weight(1f),
                        label = "",
                        selectedItem = workStartYearValue,
                        items = yearList,
                        onItemSelected = {onWorkStartYearValueChange(it)},
                        itemToString = {it},
                        placeholder = stringResource(R.string.document_workplace_info_3_work_start_year_placeholder),
                        trailingText = stringResource(R.string.document_workplace_info_3_work_start_year_trailing_text)
                    )
                    Spacer(Modifier.width(13.dp))
                    HelpJobDropdown(
                        modifier = Modifier
                            .weight(1f),
                        label = "",
                        selectedItem = workStartMonthValue,
                        items = monthList,
                        onItemSelected = {onWorkStartMonthValueChange(it)},
                        itemToString = {it},
                        placeholder = stringResource(R.string.document_workplace_info_3_work_start_month_placeholder),
                        trailingText = stringResource(R.string.document_workplace_info_3_work_start_month_trailing_text)
                    )
                    Spacer(Modifier.width(13.dp))
                    HelpJobDropdown(
                        modifier = Modifier
                            .weight(1f),
                        label = "",
                        selectedItem = workStartDayValue,
                        items = dayList,
                        onItemSelected = {onWorkStartDayValueChange(it)},
                        itemToString = {it},
                        placeholder = stringResource(R.string.document_workplace_info_3_work_start_day_placeholder),
                        trailingText = stringResource(R.string.document_workplace_info_3_work_start_day_trailing_text)
                    )
                }
            }
            Spacer(Modifier.height(27.dp))
            Column {
                Text(
                    text = stringResource(R.string.document_workplace_info_3_work_end_day_label),
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey500,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row {
                    HelpJobDropdown(
                        modifier = Modifier
                            .weight(1f),
                        label = "",
                        selectedItem = workEndYearValue,
                        items = yearList,
                        onItemSelected = {onWorkEndYearValueChange(it)},
                        itemToString = {it},
                        placeholder = stringResource(R.string.document_workplace_info_3_work_end_year_placeholder),
                        trailingText = stringResource(R.string.document_workplace_info_3_work_end_year_trailing_text),
                        isUpward = true
                    )
                    Spacer(Modifier.width(13.dp))
                    HelpJobDropdown(
                        modifier = Modifier
                            .weight(1f),
                        label = "",
                        selectedItem = workEndMonthValue,
                        items = monthList,
                        onItemSelected = {onWorkEndMonthValueChange(it)},
                        itemToString = {it},
                        placeholder = stringResource(R.string.document_workplace_info_3_work_end_month_placeholder),
                        trailingText = stringResource(R.string.document_workplace_info_3_work_end_month_trailing_text),
                        isUpward = true
                    )
                    Spacer(Modifier.width(13.dp))
                    HelpJobDropdown(
                        modifier = Modifier
                            .weight(1f),
                        label = "",
                        selectedItem = workEndDayValue,
                        items = dayList,
                        onItemSelected = {onWorkEndDayValueChange(it)},
                        itemToString = {it},
                        placeholder = stringResource(R.string.document_workplace_info_3_work_end_day_placeholder),
                        trailingText = stringResource(R.string.document_workplace_info_3_work_end_day_trailing_text),
                        isUpward = true
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun WorkplaceInfo3Preview(

){
    HelpJobTheme {
        WorkplaceInfo3Screen (
            modifier = Modifier
                .fillMaxSize(),
            step = 2,
            title = "취업 예정 근무처 정보를 입력해주세요",
            enabled = false,
            onNext = {},
            hourlyWageValue = "",
            onHourlyWageValueChange = {},
            workStartYearValue = "",
            onWorkStartYearValueChange = {},
            workStartMonthValue = "",
            onWorkStartMonthValueChange = {},
            workStartDayValue = "",
            onWorkStartDayValueChange = {},
            workEndYearValue = "",
            onWorkEndYearValueChange = {},
            workEndMonthValue = "",
            onWorkEndMonthValueChange = {},
            workEndDayValue = "",
            onWorkEndDayValueChange = {},
        )
    }
}