package unithon.helpjob.ui.document.page

import BusinessNumberVisualTransformation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.data.model.Business
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.components.HelpJobTextField

@Composable
fun WorkplaceInfo1Screen(
    modifier: Modifier = Modifier,
    step: Int,
    title: String,
    companyNameValue: String,
    onCompanyNameValueChange: (String) -> Unit,
    businessRegisterNumberValue: String,
    onBusinessRegisterNumberValueChange: (String) -> Unit,
    categoryOfBusinessValue: String,
    onCategoryOfBusinessValueChange: (String) -> Unit,
    enabled: Boolean,
    onNext: () -> Unit
){
    val context = LocalContext.current
    val businessList = Business.entries
    val selectedBusiness = Business.fromApiValue(categoryOfBusinessValue)

    DocumentInfoScreen(
        modifier = modifier,
        step = step,
        title = title,
        enabled = enabled,
        onNext = onNext
    ) {
        Column {
            HelpJobTextField(
                value = companyNameValue,
                onValueChange = onCompanyNameValueChange,
                label = stringResource(R.string.document_workplace_info_1_company_name_label),
                placeholder = stringResource(R.string.document_workplace_info_1_company_name_placeholder),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(Modifier.height(27.dp))

            HelpJobTextField(
                value = businessRegisterNumberValue,
                onValueChange = onBusinessRegisterNumberValueChange,
                label = stringResource(R.string.document_workplace_info_1_business_number_label),
                placeholder = stringResource(R.string.document_workplace_info_1_business_number_placeholder),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done,keyboardType = KeyboardType.Number),
                visualTransformation = BusinessNumberVisualTransformation()
            )

            Spacer(Modifier.height(27.dp))

            HelpJobDropdown(
                selectedItem = selectedBusiness,
                items = businessList,
                onItemSelected = { business ->
                    onCategoryOfBusinessValueChange(business.apiValue)
                },
                label = stringResource(R.string.document_workplace_info_1_category_of_business_label),
                placeholder = stringResource(R.string.document_workplace_info_1_category_of_business_placeholder),
                itemToString = { business ->
                    // 간단하고 확실한 방법
                    business.getDisplayName(context)
                },
                isUpward = true
            )
        }
    }
}