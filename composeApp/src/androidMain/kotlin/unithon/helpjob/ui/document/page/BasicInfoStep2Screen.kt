package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import unithon.helpjob.data.model.Semester
import unithon.helpjob.resources.MR
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
                label = stringResource(MR.strings.document_basic_info_2_semester_label),
                placeholder = stringResource(MR.strings.document_basic_info_2_semester_placeholder),
                itemToString = { semester ->
                    // 🆕 Context를 통해 현재 언어에 맞는 표시 이름 반환
                    semester.getDisplayName(context)
                }
            )
            Spacer(Modifier.height(27.dp))
            DocumentPhoneNumberTextField(
                value = phoneNumberValue,
                onValueChange = onPhoneNumberValueChange,
                labelText = stringResource(MR.strings.document_basic_info_2_phone_number_label),
                placeholderText = stringResource(MR.strings.document_basic_info_2_phone_number_placeholder),
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(27.dp))
            DocumentEmailTextField(
                value = emailAddressValue,
                onValueChange = onEmailAddressValueChange,
                labelText = stringResource(MR.strings.document_basic_info_2_email_address_label),
                placeholderText = stringResource(MR.strings.document_basic_info_2_email_address_placeholder),
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