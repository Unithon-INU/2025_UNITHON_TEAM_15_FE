package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.Primary500

@Composable
fun DocumentInfoScreen(
    modifier: Modifier = Modifier,
    step: Int,
    title: String,
    enabled: Boolean,
    contentPadding: Dp = 39.dp,
    verticalScrollable: Boolean = true,
    onNext: () -> Unit,
    content: @Composable () -> Unit,
){
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = if (verticalScrollable) {
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .align(Alignment.TopStart)
            } else {
                Modifier
                    .fillMaxSize()
                    .align(Alignment.TopStart)
            },
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Spacer(Modifier.height(25.dp))
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Primary500
                    )
                ) {
                    Text(
                        text = "Step $step",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Grey600,
                        modifier = Modifier
                            .padding(vertical = 7.dp, horizontal = 16.dp)
                    )
                }
                Spacer(Modifier.height(7.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 22.sp,
                        lineHeight = 32.sp,
                    ),
                    color = Grey700
                )
                Spacer(Modifier.height(contentPadding))
                content()
            }
            HelpJobButton(
                text = stringResource(R.string.document_onboarding_next),
                onClick = onNext,
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )
        }
    }
}