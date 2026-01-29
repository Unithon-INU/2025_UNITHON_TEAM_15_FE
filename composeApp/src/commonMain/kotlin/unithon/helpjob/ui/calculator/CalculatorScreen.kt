package unithon.helpjob.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.calculator_2026
import helpjob.composeapp.generated.resources.calculator_calculate_salary
import helpjob.composeapp.generated.resources.calculator_select_time
import helpjob.composeapp.generated.resources.calculator_title_per_hour
import helpjob.composeapp.generated.resources.calculator_wage_example
import helpjob.composeapp.generated.resources.calculator_wage_label
import helpjob.composeapp.generated.resources.calculator_weekly_allowance_include_label
import helpjob.composeapp.generated.resources.calculator_weekly_allowance_include_no
import helpjob.composeapp.generated.resources.calculator_weekly_allowance_include_placeholder
import helpjob.composeapp.generated.resources.calculator_weekly_allowance_include_yes
import helpjob.composeapp.generated.resources.calculator_weekly_work_time_label
import helpjob.composeapp.generated.resources.calculator_work_time_label
import helpjob.composeapp.generated.resources.error_lower_than_minimun_wage
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.ui.calculator.components.CalculationResultDialog
import unithon.helpjob.ui.calculator.components.CalculatorWageTextField
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Primary200
import unithon.helpjob.ui.theme.Primary400

/**
 * 급여 계산기 화면 (KMP 공통)
 */
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CalculatorScreenContent(
        uiState = uiState,
        workTimeOptions = viewModel.workTimeOptions,
        workDayOptions = viewModel.workDayOptions,
        weeklyAllowanceOptions = viewModel.weeklyAllowanceOptions,
        onWageChange = viewModel::updateWage,
        onWorkTimeSelected = viewModel::updateSelectedWorkTime,
        onWorkDayCountSelected = viewModel::updateSelectedWorkDayCount,
        onWeeklyAllowanceSelected = viewModel::updateIncludeWeeklyAllowance,
        onCalculateClick = viewModel::calculateSalary,
        onResultDialogDismiss = viewModel::dismissResultDialog,
        modifier = modifier
    )
}

/**
 * 계산기 화면 UI 컨텐츠
 */
@Composable
internal fun CalculatorScreenContent(
    uiState: CalculatorViewModel.CalculatorUiState,
    workTimeOptions: List<Float>,
    workDayOptions: List<Int>,
    weeklyAllowanceOptions: List<Boolean>,
    onWageChange: (String) -> Unit,
    onWorkTimeSelected: (Float) -> Unit,
    onWorkDayCountSelected: (Int) -> Unit,
    onWeeklyAllowanceSelected: (Boolean) -> Unit,
    onCalculateClick: () -> Unit,
    onResultDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
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
                labelText = stringResource(Res.string.calculator_wage_label),
                placeholderText = stringResource(Res.string.calculator_wage_example),
                isError = uiState.isLowerThanMinimumWage,
                errorMessage = if (uiState.isLowerThanMinimumWage) {
                    stringResource(Res.string.error_lower_than_minimun_wage)
                } else null,
                onDone = { focusManager.clearFocus() }
            )

            if (!uiState.isLowerThanMinimumWage) {
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(15.dp))

            // 미리 포맷된 문자열 맵 생성
            val workTimeDisplayMap = workTimeOptions.associateWith { formatWorkTime(it) }
            val workDayDisplayMap = workDayOptions.associateWith { formatWorkDays(it) }
            val allowanceYesText = stringResource(Res.string.calculator_weekly_allowance_include_yes)
            val allowanceNoText = stringResource(Res.string.calculator_weekly_allowance_include_no)
            val weeklyAllowanceDisplayMap = weeklyAllowanceOptions.associateWith { include ->
                if (include) allowanceYesText else allowanceNoText
            }

            // 일일 근무시간 드롭다운
            HelpJobDropdown(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.calculator_work_time_label),
                selectedItem = uiState.selectedWorkTime,
                items = workTimeOptions,
                onItemSelected = onWorkTimeSelected,
                itemToString = { time -> workTimeDisplayMap[time] ?: "" },
                placeholder = stringResource(Res.string.calculator_select_time),
                labelTextFieldSpace = 9.dp,
                isUpward = false
            )

            Spacer(Modifier.height(27.dp))

            // 주간 근무일수 드롭다운
            HelpJobDropdown(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.calculator_weekly_work_time_label),
                selectedItem = uiState.selectedWorkDayCount,
                items = workDayOptions,
                onItemSelected = onWorkDayCountSelected,
                itemToString = { days -> workDayDisplayMap[days] ?: "" },
                placeholder = stringResource(Res.string.calculator_select_time),
                labelTextFieldSpace = 9.dp,
                isUpward = false
            )

            Spacer(Modifier.height(27.dp))

            // 주휴수당 포함 여부 드롭다운
            HelpJobDropdown(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.calculator_weekly_allowance_include_label),
                selectedItem = uiState.includeWeeklyAllowance,
                items = weeklyAllowanceOptions,
                onItemSelected = onWeeklyAllowanceSelected,
                itemToString = { include -> weeklyAllowanceDisplayMap[include] ?: "" },
                placeholder = stringResource(Res.string.calculator_weekly_allowance_include_placeholder),
                labelTextFieldSpace = 9.dp,
                isUpward = true
            )

            // 버튼을 위한 여백
            Spacer(Modifier.height(100.dp))
        }

        // 계산하기 버튼
        HelpJobButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                )
                .fillMaxWidth(),
            text = stringResource(Res.string.calculator_calculate_salary),
            onClick = onCalculateClick,
            enabled = uiState.isWorkTimeInputValid &&
                    uiState.isWorkDayCountInputValid &&
                    uiState.isWageInputValid &&
                    uiState.isWeeklyAllowanceInputValid
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
                    text = stringResource(Res.string.calculator_2026),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Grey600
                    ),
                    modifier = Modifier
                        .padding(vertical = 7.dp, horizontal = 13.dp)
                )
            }
            Spacer(Modifier.height(9.dp))
            Text(
                text = stringResource(Res.string.calculator_title_per_hour),
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Grey600
                )
            )
        }
    }
}

// formatWorkTime, formatWorkDays는 플랫폼별 구현으로 이동됨
// CalculatorStringFormatter.kt (expect/actual 패턴 사용)
