@file:JvmName("WorkplaceInfo2ScreenKt")

package unithon.helpjob.ui.document.page

import PhoneNumberVisualTransformation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobTextField

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

            HelpJobTextField(
                value = companyAddressValue,
                onValueChange = onCompanyAddressValueChange,
                label = stringResource(R.string.document_workplace_info_2_company_address_label),
                placeholder = stringResource(R.string.document_workplace_info_2_company_address_placeholder),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(Modifier.height(27.dp))
            HelpJobTextField(
                value = employerNameValue,
                onValueChange = onEmployerNameValueChange,
                label = stringResource(R.string.document_workplace_info_2_employer_name_label),
                placeholder = stringResource(R.string.document_workplace_info_2_employer_name_placeholder),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(Modifier.height(27.dp))
            HelpJobTextField(
                value = employerPhoneNumberValue,
                onValueChange = onEmployerPhoneNumberValueChange,
                label = stringResource(R.string.document_workplace_info_2_employer_phone_number_label),
                placeholder = stringResource(R.string.document_workplace_info_2_employer_phone_number_placeholder),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = PhoneNumberVisualTransformation()
            )
        }
    }
}