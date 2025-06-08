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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.HelpJobTheme

@Composable
fun EmailCheckScreen(
    modifier: Modifier = Modifier,
    emailAddressValue: String,
    emailAddressValueChange: (String) -> Unit,
    enabled: Boolean,
    onNext: () -> Unit,
){
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp),
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
            HelpJobTextField(
                modifier = Modifier.fillMaxWidth(),
                value = emailAddressValue,
                onValueChange = emailAddressValueChange,
                label = stringResource(R.string.document_email_check_label)
            )
        }
        HelpJobButton(
            text = stringResource(R.string.document_onboarding_next),
            onClick = onNext,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun EmailCheckPreview(

){
    HelpJobTheme {
        EmailCheckScreen(
            modifier = Modifier
                .fillMaxSize(),
            enabled = false,
            onNext = {},
            emailAddressValue = "ladonna.gregory@example.com",
            emailAddressValueChange = {},
        )
    }
}