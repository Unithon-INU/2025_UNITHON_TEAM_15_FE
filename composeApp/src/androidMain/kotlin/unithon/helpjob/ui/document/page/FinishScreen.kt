package unithon.helpjob.ui.document.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_finish_button
import helpjob.composeapp.generated.resources.document_finish_description
import helpjob.composeapp.generated.resources.document_finish_title
import helpjob.composeapp.generated.resources.message
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey700

@Composable
fun FinishScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.message),
                contentDescription = "메시지",
                modifier = Modifier
                    .padding(horizontal = 68.dp)
            )
            Spacer(Modifier.height(25.dp))
            Text(
                text = stringResource(Res.string.document_finish_title),
                style = MaterialTheme.typography.headlineLarge,
                color = Grey700
            )
            Spacer(Modifier.height(9.dp))
            Text(
                text = stringResource(Res.string.document_finish_description),
                style = MaterialTheme.typography.titleMedium,
                color = Grey500
            )
        }
        HelpJobButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            text = stringResource(Res.string.document_finish_button),
            onClick = onNext,
            enabled = true
        )
    }
}