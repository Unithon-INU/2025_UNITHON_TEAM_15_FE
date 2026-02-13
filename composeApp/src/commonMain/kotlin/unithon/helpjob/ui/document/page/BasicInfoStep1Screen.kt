package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_basic_info_1_foreigner_register_number_label
import helpjob.composeapp.generated.resources.document_basic_info_1_foreigner_register_number_placeholder
import helpjob.composeapp.generated.resources.document_basic_info_1_name_label
import helpjob.composeapp.generated.resources.document_basic_info_1_name_placeholder
import helpjob.composeapp.generated.resources.document_basic_info_2_phone_number_label
import helpjob.composeapp.generated.resources.document_basic_info_2_phone_number_placeholder
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.ui.document.components.DocumentForeignerNumberTextField
import unithon.helpjob.ui.document.components.DocumentPhoneNumberTextField
import unithon.helpjob.ui.document.components.DocumentTextTextField

@Composable
fun BasicInfoStep1Screen(
    modifier: Modifier = Modifier,
    step: Int,
    title: String,
    nameValue: String,
    onNameValueChange: (String) -> Unit,
    foreignerNumberValue: String,
    onForeignerNumberValueChange: (String) -> Unit,
    phoneNumberValue: String,
    onPhoneNumberValueChange: (String) -> Unit,
    phoneError: Boolean = false,
    phoneErrorMessage: StringResource? = null,
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
        Column {
            DocumentTextTextField(
                value = nameValue,
                onValueChange = onNameValueChange,
                labelText = stringResource(Res.string.document_basic_info_1_name_label),
                placeholderText = stringResource(Res.string.document_basic_info_1_name_placeholder),
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(27.dp))

            DocumentForeignerNumberTextField(
                value = foreignerNumberValue,
                onValueChange = onForeignerNumberValueChange,
                labelText = stringResource(Res.string.document_basic_info_1_foreigner_register_number_label),
                placeholderText = stringResource(Res.string.document_basic_info_1_foreigner_register_number_placeholder),
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(27.dp))

            DocumentPhoneNumberTextField(
                value = phoneNumberValue,
                onValueChange = onPhoneNumberValueChange,
                labelText = stringResource(Res.string.document_basic_info_2_phone_number_label),
                placeholderText = stringResource(Res.string.document_basic_info_2_phone_number_placeholder),
                imeAction = ImeAction.Done,
                onImeAction = if (enabled) onNext else null,
                isError = phoneError,
                errorMessage = phoneErrorMessage?.let { stringResource(it) }
            )
        }


    }
}