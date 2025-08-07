package unithon.helpjob.ui.calculator.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Warning
import unithon.helpjob.util.CurrencyVisualTransformation

/**
 * 계산기용 시급 입력 텍스트필드
 */
@Composable
fun CalculatorWageTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String,
    placeholderText: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    onDone: (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Done
) {
    Column(modifier = modifier) {
        HelpJobTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = if (labelText.isNotBlank()) {
                {
                    Text(
                        text = labelText,
                        style = MaterialTheme.typography.titleSmall,
                        color = Grey500,
                        modifier = Modifier.padding(bottom = 9.dp)
                    )
                }
            } else null,
            placeholder = if (placeholderText.isNotBlank()) {
                {
                    Text(
                        text = placeholderText,
                        style = MaterialTheme.typography.titleSmall,
                        color = Grey300
                    )
                }
            } else null,
            trailingIcon = {
                Text(
                    text = stringResource(R.string.calculator_won),
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey600
                )
            },
            visualTransformation = CurrencyVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = { onDone?.invoke() }
            ),
            isError = isError
        )

        // 에러 메시지
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Warning,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}