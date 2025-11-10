package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import unithon.helpjob.data.model.Business
import unithon.helpjob.resources.MR
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.document.components.DocumentBusinessNumberTextField
import unithon.helpjob.ui.document.components.DocumentTextTextField
import unithon.helpjob.ui.theme.HelpJobTheme

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
            DocumentTextTextField(
                value = companyNameValue,
                onValueChange = onCompanyNameValueChange,
                labelText = stringResource(MR.strings.document_workplace_info_1_company_name_label),
                placeholderText = stringResource(MR.strings.document_workplace_info_1_company_name_placeholder),
                imeAction = ImeAction.Next
            )

            Spacer(Modifier.height(27.dp))

            DocumentBusinessNumberTextField(
                value = businessRegisterNumberValue,
                onValueChange = onBusinessRegisterNumberValueChange,
                labelText = stringResource(MR.strings.document_workplace_info_1_business_number_label),
                placeholderText = stringResource(MR.strings.document_workplace_info_1_business_number_placeholder),
                imeAction = ImeAction.Next
            )

            Spacer(Modifier.height(27.dp))

            HelpJobDropdown(
                selectedItem = selectedBusiness,
                items = businessList,
                onItemSelected = { business ->
                    onCategoryOfBusinessValueChange(business.apiValue)
                },
                label = stringResource(MR.strings.document_workplace_info_1_category_of_business_label),
                placeholder = stringResource(MR.strings.document_workplace_info_1_category_of_business_placeholder),
                itemToString = { business ->
                    // 간단하고 확실한 방법
                    business.getDisplayName(context)
                },
                isUpward = true
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ko")
@Composable
fun WorkplaceInfo1ScreenPreview(){
    HelpJobTheme {
        WorkplaceInfo1Screen(
            step = 1,
            title = stringResource(MR.strings.document_step_2_title),
            companyNameValue = "",
            onCompanyNameValueChange = {},
            businessRegisterNumberValue = "",
            onBusinessRegisterNumberValueChange = {},
            categoryOfBusinessValue = "",
            onCategoryOfBusinessValueChange = {},
            enabled = false,
            onNext = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ko")
@Composable
fun WorkplaceInfo1ScreenFilledPreview(){
    HelpJobTheme {
        WorkplaceInfo1Screen(
            step = 1,
            title = stringResource(MR.strings.document_step_2_title),
            companyNameValue = "오토그룹",
            onCompanyNameValueChange = {},
            businessRegisterNumberValue = "1234567890",
            onBusinessRegisterNumberValueChange = {},
            categoryOfBusinessValue = "RESTAURANT",
            onCategoryOfBusinessValueChange = {},
            enabled = true,
            onNext = {}
        )
    }
}