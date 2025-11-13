package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_basic_info_2_email_address_label
import helpjob.composeapp.generated.resources.document_basic_info_2_email_address_placeholder
import helpjob.composeapp.generated.resources.document_basic_info_2_phone_number_label
import helpjob.composeapp.generated.resources.document_basic_info_2_phone_number_placeholder
import helpjob.composeapp.generated.resources.document_basic_info_2_semester_label
import helpjob.composeapp.generated.resources.document_basic_info_2_semester_placeholder
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.data.model.Semester
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.document.components.DocumentEmailTextField
import unithon.helpjob.ui.document.components.DocumentPhoneNumberTextField
import unithon.helpjob.ui.theme.HelpJobTheme

@Composable
fun BasicInfoStep2Screen(
    modifier: Modifier = Modifier,
    emailError: Boolean,  // 추가
    emailErrorMessage: StringResource?,  // 수정: Int? → StringResource?
    step: Int,
    title: String,
    semesterValue: Semester?,
    onSemesterValueChange: (Semester) -> Unit,
    phoneNumberValue: String,
    onPhoneNumberValueChange: (String) -> Unit,
    emailAddressValue: String,
    onEmailAddressValueChange: (String) -> Unit,
    enabled: Boolean,
    onNext: () -> Unit
){
    val context = LocalContext.current
    val semesterList = Semester.entries
    // 🆕 미리 displayName 맵 생성 (@Composable 컨텍스트에서)
    val semesterDisplayMap = remember {
        semesterList.associateWith { "" }
    }
    val semesterDisplayMapUpdated = semesterList.associateWith { it.getDisplayName() }

    DocumentInfoScreen(
        modifier = modifier,
        step = step,
        title = title,
        enabled = enabled,
        onNext = onNext
    ) {
        Column {
            HelpJobDropdown(
                selectedItem = semesterValue,
                items = semesterList,
                onItemSelected = onSemesterValueChange,
                label = stringResource(Res.string.document_basic_info_2_semester_label),
                placeholder = stringResource(Res.string.document_basic_info_2_semester_placeholder),
                itemToString = { semester ->
                    // 미리 생성된 맵에서 가져오기
                    semesterDisplayMapUpdated[semester] ?: ""
                }
            )
            Spacer(Modifier.height(27.dp))
            DocumentPhoneNumberTextField(
                value = phoneNumberValue,
                onValueChange = onPhoneNumberValueChange,
                labelText = stringResource(Res.string.document_basic_info_2_phone_number_label),
                placeholderText = stringResource(Res.string.document_basic_info_2_phone_number_placeholder),
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(27.dp))
            DocumentEmailTextField(
                value = emailAddressValue,
                onValueChange = onEmailAddressValueChange,
                labelText = stringResource(Res.string.document_basic_info_2_email_address_label),
                placeholderText = stringResource(Res.string.document_basic_info_2_email_address_placeholder),
                imeAction = ImeAction.Next,
                isError = emailError,  // 추가
                errorMessage = emailErrorMessage?.let { stringResource(it) }  // 수정: StringResource 사용
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ko")
@Composable
fun BasicInfoStep2Preview(){
    HelpJobTheme {
        BasicInfoStep2Screen(
            modifier = Modifier.fillMaxSize(),
            step = 1,
            title = "기본 정보를 입력하세요",
            enabled = false,
            onNext = {},
            semesterValue = Semester.FIRST_YEAR_FIRST,
            onSemesterValueChange = {},
            phoneNumberValue = "",
            onPhoneNumberValueChange = {},
            emailAddressValue = "freeman.spence@example.com",
            onEmailAddressValueChange = {},
            emailError = false,
            emailErrorMessage = null  // 수정: null 사용
        )
    }
}