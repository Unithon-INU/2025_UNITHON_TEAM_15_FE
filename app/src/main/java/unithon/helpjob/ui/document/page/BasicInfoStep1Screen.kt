package unithon.helpjob.ui.document.page

import ForeignerNumberVisualTransformation
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
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.HelpJobTheme

@Composable
fun BasicInfoStep1Screen(
    modifier: Modifier = Modifier,
    step: Int,
    title: String,
    nameValue: String,
    onNameValueChange: (String) -> Unit,
    foreignerNumberValue: String,
    onForeignerNumberValueChange: (String) -> Unit,
    majorValue: String,
    onMajorValueChange: (String) -> Unit,
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
                value = nameValue,
                onValueChange = onNameValueChange,
                label = stringResource(R.string.document_basic_info_1_name_label),
                placeholder = stringResource(R.string.document_basic_info_1_name_placeholder),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(Modifier.height(27.dp))
            HelpJobTextField(
                value = foreignerNumberValue,
                onValueChange = onForeignerNumberValueChange,
                label = stringResource(R.string.document_basic_info_1_foreigner_register_number_label),
                placeholder = stringResource(R.string.document_basic_info_1_foreigner_register_number_placeholder),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next,keyboardType = KeyboardType.Number),
                visualTransformation = ForeignerNumberVisualTransformation(),

            )
            Spacer(Modifier.height(27.dp))
            HelpJobTextField(
                value = majorValue,
                onValueChange = onMajorValueChange,
                label = stringResource(R.string.document_basic_info_1_major_label),
                placeholder = stringResource(R.string.document_basic_info_1_major_placeholder)
            )
        }


    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun BasicInfoStep1Preview(

){
    HelpJobTheme {
        BasicInfoStep1Screen(
            modifier = Modifier
                .fillMaxSize(),
            step = 1,
            title = "기본 정보를 입력하세요",
            enabled = false,
            onNext = {},
            nameValue = "Gayle Gonzalez",
            onNameValueChange = {},
            foreignerNumberValue = "minim",
            onForeignerNumberValueChange = {},
            majorValue = "porttitor",
            onMajorValueChange = {}
        )
    }
}