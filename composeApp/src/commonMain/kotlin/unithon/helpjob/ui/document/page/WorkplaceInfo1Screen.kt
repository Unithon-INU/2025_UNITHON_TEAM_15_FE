package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_step_2_title
import helpjob.composeapp.generated.resources.document_workplace_info_1_business_number_label
import helpjob.composeapp.generated.resources.document_workplace_info_1_business_number_placeholder
import helpjob.composeapp.generated.resources.document_workplace_info_1_category_of_business_label
import helpjob.composeapp.generated.resources.document_workplace_info_1_category_of_business_placeholder
import helpjob.composeapp.generated.resources.document_workplace_info_1_company_name_label
import helpjob.composeapp.generated.resources.document_workplace_info_1_company_name_placeholder
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.data.model.Business
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
    categoryOfBusinessValue: Business?,
    onCategoryOfBusinessValueChange: (Business) -> Unit,
    enabled: Boolean,
    onNext: () -> Unit
){
    val businessList = Business.entries
    // 🆕 미리 displayName 맵 생성 (@Composable 컨텍스트에서)
    val businessDisplayMap = businessList.associateWith { it.getDisplayName() }

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
                labelText = stringResource(Res.string.document_workplace_info_1_company_name_label),
                placeholderText = stringResource(Res.string.document_workplace_info_1_company_name_placeholder),
                imeAction = ImeAction.Next
            )

            Spacer(Modifier.height(27.dp))

            DocumentBusinessNumberTextField(
                value = businessRegisterNumberValue,
                onValueChange = onBusinessRegisterNumberValueChange,
                labelText = stringResource(Res.string.document_workplace_info_1_business_number_label),
                placeholderText = stringResource(Res.string.document_workplace_info_1_business_number_placeholder),
                imeAction = ImeAction.Done,
                onImeAction = if (enabled) onNext else null
            )

            Spacer(Modifier.height(27.dp))

            HelpJobDropdown(
                selectedItem = categoryOfBusinessValue,
                items = businessList,
                onItemSelected = { business ->
                    onCategoryOfBusinessValueChange(business)
                },
                label = stringResource(Res.string.document_workplace_info_1_category_of_business_label),
                placeholder = stringResource(Res.string.document_workplace_info_1_category_of_business_placeholder),
                itemToString = { business ->
                    // 미리 생성된 맵에서 가져오기
                    businessDisplayMap[business] ?: ""
                },
            )
        }
    }
}

// Preview functions moved to androidMain for Android-only preview support