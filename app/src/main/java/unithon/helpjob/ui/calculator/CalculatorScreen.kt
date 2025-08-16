package unithon.helpjob.ui.calculator

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.data.repository.LanguageAwareScreen
import unithon.helpjob.ui.calculator.components.CalculationResult
import unithon.helpjob.ui.calculator.components.CalculationResultDialog
import unithon.helpjob.ui.calculator.components.CalculatorWageTextField
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.HelpJobTheme
import unithon.helpjob.ui.theme.Primary200
import unithon.helpjob.ui.theme.Primary400

@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CalculatorScreenContent(
        uiState = uiState,
        workTimeOptions = viewModel.workTimeOptions,
        workDayOptions = viewModel.workDayOptions,
        onWageChange = viewModel::updateWage,
        onWorkTimeSelected = viewModel::updateSelectedWorkTime,
        onWorkDayCountSelected = viewModel::updateSelectedWorkDayCount,
        onCalculateClick = viewModel::calculateSalary,
        onResultDialogDismiss = viewModel::dismissResultDialog,
        modifier = modifier
    )
}

/**
 * 계산기 화면 UI 컨텐츠 (프리뷰 지원을 위해 분리)
 */
@Composable
private fun CalculatorScreenContent(
    uiState: CalculatorViewModel.CalculatorUiState,
    workTimeOptions: List<Float>,
    workDayOptions: List<Int>,
    onWageChange: (String) -> Unit,
    onWorkTimeSelected: (Float) -> Unit,
    onWorkDayCountSelected: (Int) -> Unit,
    onCalculateClick: () -> Unit,
    onResultDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LanguageAwareScreen {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scrollState)
                    .align(Alignment.TopCenter)
            ) {
                Spacer(Modifier.height(39.dp))

                // 최저시급 카드
                MinimumWageCard(
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(26.dp))

                // 시급 입력 텍스트필드
                CalculatorWageTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.wage,
                    onValueChange = onWageChange,
                    labelText = stringResource(R.string.calculator_wage_label),
                    placeholderText = stringResource(R.string.calculator_wage_example),
                    isError = uiState.isLowerThanMinimumWage,
                    errorMessage = if (uiState.isLowerThanMinimumWage) {
                        stringResource(R.string.error_lower_than_minimun_wage)
                    } else null,
                    onDone = { focusManager.clearFocus() }
                )

                if (!uiState.isLowerThanMinimumWage) {
                    Spacer(Modifier.height(12.dp))
                }

                Spacer(Modifier.height(15.dp))

                // 일일 근무시간 드롭다운
                HelpJobDropdown(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.calculator_work_time_label),
                    selectedItem = uiState.selectedWorkTime,
                    items = workTimeOptions,
                    onItemSelected = onWorkTimeSelected,
                    itemToString = { time -> formatWorkTime(time, context) },
                    placeholder = stringResource(R.string.calculator_select_time),
                    labelTextFieldSpace = 9.dp,
                    isUpward = false
                )

                Spacer(Modifier.height(27.dp))

                // 주간 근무일수 드롭다운
                HelpJobDropdown(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.calculator_weekly_work_time_label),
                    selectedItem = uiState.selectedWorkDayCount,
                    items = workDayOptions,
                    onItemSelected = onWorkDayCountSelected,
                    itemToString = { days -> formatWorkDays(days, context) },
                    placeholder = stringResource(R.string.calculator_select_time),
                    labelTextFieldSpace = 9.dp,
                    isUpward = true
                )

                // 버튼을 위한 여백
                Spacer(Modifier.height(100.dp))
            }

            // 계산하기 버튼
            HelpJobButton(
                textRes = R.string.calculator_calculate_salary,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 20.dp
                    )
                    .fillMaxWidth(),
                onClick = onCalculateClick,
                enabled = uiState.isWorkTimeInputValid &&
                        uiState.isWorkDayCountInputValid &&
                        uiState.isWageInputValid
            )
        }

        // 결과 다이얼로그
        if (uiState.showResultDialog) {
            CalculationResultDialog(
                result = uiState.calculationResult,
                onDismiss = onResultDialogDismiss
            )
        }
    }
}

@Composable
private fun MinimumWageCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Primary400
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 20.dp,
                    horizontal = 23.dp
                )
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Primary200
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.calculator_2025),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Grey600
                    ),
                    modifier = Modifier
                        .padding(vertical = 7.dp, horizontal = 13.dp)
                )
            }
            Spacer(Modifier.height(9.dp))
            Text(
                text = stringResource(R.string.calculator_title_per_hour),
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Grey600
                )
            )
        }
    }
}

private fun formatWorkTime(time: Float, context: Context): String {
    val hours = time.toInt()
    val minutes = ((time - hours) * 60).toInt()

    // Context가 자동으로 현재 언어의 올바른 리소스를 선택함
    return if (minutes == 0) {
        if (hours == 1) {
            context.getString(R.string.calculator_hours_format_singular, hours)
        } else {
            context.getString(R.string.calculator_hours_format_plural, hours)
        }
    } else {
        when {
            hours == 1 && minutes == 1 ->
                context.getString(R.string.calculator_hours_minutes_format_singular_singular, hours, minutes)
            hours == 1 && minutes > 1 ->
                context.getString(R.string.calculator_hours_minutes_format_singular_plural, hours, minutes)
            hours > 1 && minutes == 1 ->
                context.getString(R.string.calculator_hours_minutes_format_plural_singular, hours, minutes)
            else ->
                context.getString(R.string.calculator_hours_minutes_format_plural_plural, hours, minutes)
        }
    }
}

private fun formatWorkDays(days: Int, context: Context): String {
    return if (days == 1) {
        context.getString(R.string.calculator_days_format_singular, days)
    } else {
        context.getString(R.string.calculator_days_format_plural, days)
    }
}

// =================================
// 프리뷰들
// =================================

// 기본 상태 프리뷰
@Preview(
    name = "기본 상태",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    locale = "ko"
)
@Composable
fun CalculatorScreenContentPreview() {
    HelpJobTheme {
        CalculatorScreenContent(
            uiState = CalculatorViewModel.CalculatorUiState(),
            workTimeOptions = listOf(1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f),
            workDayOptions = listOf(1, 2, 3, 4, 5, 6, 7),
            onWageChange = {},
            onWorkTimeSelected = {},
            onWorkDayCountSelected = {},
            onCalculateClick = {},
            onResultDialogDismiss = {}
        )
    }
}

// 입력된 상태 프리뷰
@Preview(
    name = "입력된 상태",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    locale = "ko"
)
@Composable
fun CalculatorScreenContentInputPreview() {
    HelpJobTheme {
        CalculatorScreenContent(
            uiState = CalculatorViewModel.CalculatorUiState(
                wage = "15000",
                selectedWorkTime = 8.0f,
                selectedWorkDayCount = 5
            ),
            workTimeOptions = listOf(1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f),
            workDayOptions = listOf(1, 2, 3, 4, 5, 6, 7),
            onWageChange = {},
            onWorkTimeSelected = {},
            onWorkDayCountSelected = {},
            onCalculateClick = {},
            onResultDialogDismiss = {}
        )
    }
}

// 에러 상태 프리뷰 (최저시급 미만)
@Preview(
    name = "에러 상태 - 최저시급 미만",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    locale = "ko"
)
@Composable
fun CalculatorScreenContentErrorPreview() {
    HelpJobTheme {
        CalculatorScreenContent(
            uiState = CalculatorViewModel.CalculatorUiState(
                wage = "9000", // 최저시급 미만
                selectedWorkTime = 4.0f,
                selectedWorkDayCount = 3
            ),
            workTimeOptions = listOf(1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f),
            workDayOptions = listOf(1, 2, 3, 4, 5, 6, 7),
            onWageChange = {},
            onWorkTimeSelected = {},
            onWorkDayCountSelected = {},
            onCalculateClick = {},
            onResultDialogDismiss = {}
        )
    }
}

// 결과 표시 상태 프리뷰
@Preview(
    name = "결과 표시 상태",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    locale = "ko"
)
@Composable
fun CalculatorScreenContentResultPreview() {
    HelpJobTheme {
        CalculatorScreenContent(
            uiState = CalculatorViewModel.CalculatorUiState(
                wage = "15000",
                selectedWorkTime = 8.0f,
                selectedWorkDayCount = 5,
                salary = "2600000",
                calculationResult = CalculationResult(
                    workHours = 40,
                    weeklyAllowanceHours = 120000,
                    totalAmount = 2600000,
                    includesWeeklyAllowance = true
                ),
                showResultDialog = true
            ),
            workTimeOptions = listOf(1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f),
            workDayOptions = listOf(1, 2, 3, 4, 5, 6, 7),
            onWageChange = {},
            onWorkTimeSelected = {},
            onWorkDayCountSelected = {},
            onCalculateClick = {},
            onResultDialogDismiss = {}
        )
    }
}
