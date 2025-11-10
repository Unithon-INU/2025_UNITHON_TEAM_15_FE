package unithon.helpjob.ui.document.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import unithon.helpjob.R
import unithon.helpjob.resources.MR
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.HelpJobTheme
import unithon.helpjob.ui.theme.Primary100
import unithon.helpjob.ui.theme.Primary300
import unithon.helpjob.ui.theme.Primary500

@Composable
fun DocumentOnboardingScreen(
    title: String,
    image: Int,
    description: String,
    currentPage: Int,
    pageSize: Int = 2,
    onNext: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary100)
            .padding(horizontal = 20.dp)
    ){
        Column(
            modifier = Modifier
                .align(Alignment.TopStart),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(

            ) {
                Spacer(Modifier.height(90.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Grey600
                )

            }

        }
        Image(
            painter = painterResource(image),
            contentDescription = "온보딩 이미지",
            modifier = Modifier
                .padding(horizontal = 70.dp)
                .align(Alignment.Center)
        )
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Primary300
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 13.dp, vertical = 22.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.dot),
                        contentDescription = "점",
                        modifier = Modifier
                            .padding(top = 5.dp, end = 10.dp)
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.titleSmall,
                        color = Grey600
                    )
                }
            }
            Spacer(Modifier.height(17.dp))

            // 인디케이터
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pageSize) { iteration ->
                    val color = if (currentPage == iteration + 1) {
                        Primary500  // 현재 페이지와 일치하면 Primary 색상
                    } else {
                        Grey300     // 일치하지 않으면 회색
                    }
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
            Spacer(Modifier.height(29.dp))
            HelpJobButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(MR.strings.onboarding_next_button),
                onClick = onNext
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DocumentOnboardingScreenPreview(

){
    HelpJobTheme {
        DocumentOnboardingScreen(
            title = stringResource(MR.strings.document_onboarding_title) ,
            image = R.drawable.memo,
            description = stringResource(MR.strings.document_onboarding_description_2),
            currentPage = 1
        ) { }
    }
}