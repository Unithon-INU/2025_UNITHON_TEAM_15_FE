package unithon.helpjob.ui.auth.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey700

@Composable
fun SignUpSuccessScreen(
    onGoToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        // 중앙 컨텐츠
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 성공 이미지
            Image(
                painter = painterResource(id = R.drawable.signup_finish),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(27.dp))

            // 메인 타이틀
            Text(
                text = stringResource(id = R.string.signup_success_title),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Grey700,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(9.dp))

            // 서브 타이틀
            Text(
                text = stringResource(id = R.string.signup_success_subtitle),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Grey500,
                    textAlign = TextAlign.Center
                )
            )
        }

        // 하단 버튼
        HelpJobButton(
            text = stringResource(id = R.string.signup_success_go_to_login),
            onClick = onGoToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }
}