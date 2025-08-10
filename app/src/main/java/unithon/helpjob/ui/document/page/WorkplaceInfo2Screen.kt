@file:JvmName("WorkplaceInfo2ScreenKt")

package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.ui.document.components.DocumentPhoneNumberTextField
import unithon.helpjob.ui.document.components.DocumentTextTextField

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
                labelText = stringResource(R.string.document_workplace_info_2_company_address_label),
                placeholderText = stringResource(R.string.document_workplace_info_2_company_address_placeholder),
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(27.dp))
            DocumentTextTextField(
                value = employerNameValue,
                onValueChange = onEmployerNameValueChange,
                labelText = stringResource(R.string.document_workplace_info_2_employer_name_label),
                placeholderText = stringResource(R.string.document_workplace_info_2_employer_name_placeholder),
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(27.dp))
            DocumentPhoneNumberTextField(
                value = employerPhoneNumberValue,
                onValueChange = onEmployerPhoneNumberValueChange,
                labelText = stringResource(R.string.document_workplace_info_2_employer_phone_number_label),
                placeholderText = stringResource(R.string.document_workplace_info_2_employer_phone_number_placeholder),
                imeAction = ImeAction.Next
            )
        }
    }
}