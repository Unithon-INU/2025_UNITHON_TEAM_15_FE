package unithon.helpjob.ui.document.page

import PhoneNumberVisualTransformation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.HelpJobTheme

@Composable
fun BasicInfoStep2Screen(
    modifier: Modifier = Modifier,
    step: Int,
    title: String,
    semesterValue: String,
    onSemesterValueChange: (String) -> Unit,
    phoneNumberValue: String,
    onPhoneNumberValueChange: (String) -> Unit,
    emailAddressValue: String,
    onEmailAddressValueChange: (String) -> Unit,
    enabled: Boolean,
    onNext: () -> Unit
){
    val semesterList = listOf(
        "1학년 1학기",
        "1학년 2학기",
        "2학년 1학기",
        "2학년 2학기",
        "3학년 1학기",
        "3학년 2학기",
        "4학년 1학기",
        "4학년 2학기",
    )

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
                label = stringResource(R.string.document_basic_info_2_semester_label),
                placeholder = stringResource(R.string.document_basic_info_2_semester_placeholder),
                itemToString = {it}
            )
            Spacer(Modifier.height(27.dp))
            HelpJobTextField(
                value = phoneNumberValue,
                onValueChange = onPhoneNumberValueChange,
                label = stringResource(R.string.document_basic_info_2_phone_number_label),
                placeholder = stringResource(R.string.document_basic_info_2_phone_number_placeholder),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next,keyboardType = KeyboardType.Number),
                visualTransformation = PhoneNumberVisualTransformation()
            )
            Spacer(Modifier.height(27.dp))
            HelpJobTextField(
                value = emailAddressValue,
                onValueChange = onEmailAddressValueChange,
                label = stringResource(R.string.document_basic_info_2_email_address_label),
                placeholder = stringResource(R.string.document_basic_info_2_email_address_placeholder),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun BasicInfoStep2Preview(

){
    HelpJobTheme {
        BasicInfoStep2Screen(
            modifier = Modifier
                .fillMaxSize(),
            step = 1,
            title = "기본 정보를 입력하세요",
            enabled = false,
            onNext = {},
            semesterValue = "ei",
            onSemesterValueChange = {},
            phoneNumberValue = "(607) 802-8250",
            onPhoneNumberValueChange = {},
            emailAddressValue = "freeman.spence@example.com",
            onEmailAddressValueChange = {},
    
        )
    }
}