package unithon.helpjob.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Primary300
import unithon.helpjob.ui.theme.Warning
import unithon.helpjob.util.CurrencyVisualTransformation

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(scrollState)
                .align(Alignment.TopCenter)
        ) {
            Spacer(Modifier.height(124.dp))
            MinimumWageCard(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
            )
            Spacer(Modifier.height(54.dp))
            HelpJobTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                value = uiState.wage,
                onValueChange = viewModel::updateWage,
                label = stringResource(R.string.calculator_wage_label),
                placeholder = "",
                visualTransformation = CurrencyVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                isError = false,
                errorMessage = null,
                labelTextFieldSpace = 9.dp
            )
            Spacer(Modifier.height(20.dp))
            HelpJobTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                value = uiState.workTime,
                onValueChange = viewModel::updateWorkTime,
                label = stringResource(R.string.calculator_worktime_label),
                placeholder = "",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                isError = false,
                errorMessage = null,
                labelTextFieldSpace = 9.dp
            )
            Spacer(Modifier.height(20.dp))
            HelpJobTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                value = uiState.workDayCount,
                onValueChange = viewModel::updateWorkDayCount,
                label = stringResource(R.string.calculator_workday_label),
                placeholder = "",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus() // 포커스 해제
                    }
                ),
                isError = false,
                errorMessage = null,
                labelTextFieldSpace = 9.dp
            )
        }
        HelpJobButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .padding(bottom = 54.dp),
            text = stringResource(R.string.calculator_calculate_salary),
            onClick = {
                viewModel.calculateSalary()
            },
            enabled = uiState.isWorkTimeInputValid && uiState.isWorkDayCountInputValid && uiState.isWageInputValid,
        )
    }

}

@Composable
private fun MinimumWageCard(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Primary300
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 23.dp)
        ) {
            Text(
                text = stringResource(R.string.calculator_title_per_hour),
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Grey600
                )
            )
            Text(
                text = stringResource(R.string.calculator_title_minimum_wage),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Warning
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    CalculatorScreen()
}