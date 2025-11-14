package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_email_check_label
import helpjob.composeapp.generated.resources.document_email_check_title
import helpjob.composeapp.generated.resources.document_onboarding_next
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.document.components.DocumentEmailTextField
import unithon.helpjob.ui.theme.Grey700

@Composable
fun EmailCheckScreen(
    modifier: Modifier = Modifier,
    emailAddressValue: String,
    emailAddressValueChange: (String) -> Unit,
    emailError: Boolean,
    emailErrorMessage: StringResource?,
    enabled: Boolean,
    isSubmitting: Boolean = false,
    onNext: () -> Unit,
){
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column{
            Spacer(Modifier.height(19.dp))
            Text(
                text = stringResource(Res.string.document_email_check_title),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 22.sp,
                    lineHeight = 32.sp,
                ),
                color = Grey700
            )
            Spacer(Modifier.height(31.dp))
            DocumentEmailTextField(
                modifier = Modifier.fillMaxWidth(),
                value = emailAddressValue,
                onValueChange = emailAddressValueChange,
                labelText = stringResource(Res.string.document_email_check_label),
                imeAction = ImeAction.Done,
                isError = emailError,
                errorMessage = emailErrorMessage?.let { stringResource(it) }
            )
        }

        HelpJobButton(
            text = if (isSubmitting) "loading..." else stringResource(Res.string.document_onboarding_next),
            onClick = onNext,
            enabled = enabled && !isSubmitting,
            isLoading = isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )
    }
}
