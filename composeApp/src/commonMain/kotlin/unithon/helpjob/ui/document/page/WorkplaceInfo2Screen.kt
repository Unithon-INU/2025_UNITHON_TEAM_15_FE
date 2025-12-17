package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_workplace_info_2_company_address_label
import helpjob.composeapp.generated.resources.document_workplace_info_2_company_address_placeholder
import helpjob.composeapp.generated.resources.document_workplace_info_2_employer_name_label
import helpjob.composeapp.generated.resources.document_workplace_info_2_employer_name_placeholder
import helpjob.composeapp.generated.resources.document_workplace_info_2_employer_phone_number_label
import helpjob.composeapp.generated.resources.document_workplace_info_2_employer_phone_number_placeholder
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.ui.document.components.DocumentPhoneNumberTextField
import unithon.helpjob.ui.document.components.DocumentTextTextField
import unithon.helpjob.ui.theme.HelpJobTheme

@Composable
fun WorkplaceInfo2Screen(
    modifier: Modifier = Modifier,
    step: Int,
    title: String,
    companyAddressValue: String,
    onCompanyAddressValueChange: (String) -> Unit,
    employerNameValue: String,
    onEmployerNameValueChange: (String) -> Unit,
    employerPhoneNumberValue: String,
    onEmployerPhoneNumberValueChange: (String) -> Unit,
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
                value = companyAddressValue,
                onValueChange = onCompanyAddressValueChange,
                labelText = stringResource(Res.string.document_workplace_info_2_company_address_label),
                placeholderText = stringResource(Res.string.document_workplace_info_2_company_address_placeholder),
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(27.dp))
            DocumentTextTextField(
                value = employerNameValue,
                onValueChange = onEmployerNameValueChange,
                labelText = stringResource(Res.string.document_workplace_info_2_employer_name_label),
                placeholderText = stringResource(Res.string.document_workplace_info_2_employer_name_placeholder),
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(27.dp))
            DocumentPhoneNumberTextField(
                value = employerPhoneNumberValue,
                onValueChange = onEmployerPhoneNumberValueChange,
                labelText = stringResource(Res.string.document_workplace_info_2_employer_phone_number_label),
                placeholderText = stringResource(Res.string.document_workplace_info_2_employer_phone_number_placeholder),
                imeAction = ImeAction.Done,
                onImeAction = if (enabled) onNext else null
            )
        }
    }
}

// Preview functions moved to androidMain for Android-only preview support