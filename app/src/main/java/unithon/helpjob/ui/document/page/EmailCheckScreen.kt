package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.document.components.DocumentEmailTextField
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.HelpJobTheme

@Composable
fun EmailCheckScreen(
    modifier: Modifier = Modifier,
    emailAddressValue: String,
    emailAddressValueChange: (String) -> Unit,
    enabled: Boolean,
    isSubmitting: Boolean = false, // 🆕 로딩 상태 파라미터 추가
    onNext: () -> Unit,
){
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column{
            Spacer(Modifier.height(19.dp))
            Text(
                text = stringResource(R.string.document_email_check_title),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 22.sp,
                    lineHeight = 32.sp,
                ),
                color = Grey700
            )
            Spacer(Modifier.height(31.dp))
            DocumentEmailTextField(
                modifier = Modifier.fillMaxWidth(),
                value = emailAddressValue,
                onValueChange = emailAddressValueChange,
                labelText = stringResource(R.string.document_email_check_label),
                imeAction = ImeAction.Done // 이메일 입력 후 완료
            )
        }

        HelpJobButton(
            textRes = if (isSubmitting) R.string.document_email_check_loading else R.string.common_next,
            onClick = onNext,
            enabled = enabled && !isSubmitting,
            isLoading = isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ko")
@Composable
fun EmailCheckPreview(){
    HelpJobTheme {
        EmailCheckScreen(
            modifier = Modifier.fillMaxSize(),
            enabled = true,
            isSubmitting = false, // 🆕 Preview에 추가
            onNext = {},
            emailAddressValue = "ladonna.gregory@example.com",
            emailAddressValueChange = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ko")
@Composable
fun EmailCheckLoadingPreview(){
    HelpJobTheme {
        EmailCheckScreen(
            modifier = Modifier.fillMaxSize(),
            enabled = true,
            isSubmitting = true, // 🆕 로딩 상태 Preview
            onNext = {},
            emailAddressValue = "ladonna.gregory@example.com",
            emailAddressValueChange = {},
        )
    }
}