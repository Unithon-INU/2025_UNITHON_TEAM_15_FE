package unithon.helpjob.ui.auth.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.*

@Composable
fun SignInScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // 제목
        Text(
            text = "환영해요!\n000이 도와드릴게요",
            style = MaterialTheme.typography.headlineLarge, // 24sp, Bold
            color = Grey700
        )

        Spacer(modifier = Modifier.height(39.dp))

        // 이메일 입력
        Text(
            text = "이메일",
            style = MaterialTheme.typography.titleSmall, // 14sp, Bold
            color = Grey500
        )

        Spacer(modifier = Modifier.height(9.dp))

        HelpJobTextField(
            value = email,
            onValueChange = { email = it },
            label = "",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 비밀번호 입력
        Text(
            text = "비밀번호",
            style = MaterialTheme.typography.titleSmall, // 14sp, Bold
            color = Grey500
        )

        Spacer(modifier = Modifier.height(9.dp))

        HelpJobTextField(
            value = password,
            onValueChange = { password = it },
            label = "",
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(46.dp))

        // 로그인 버튼
        HelpJobButton(
            text = "로그인",
            onClick = {
                // TODO: 서버 API 완성되면 로그인 처리
                onNavigateToMain()
            },
            enabled = email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(38.dp))

        // 또는 구분선
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 왼쪽 실선
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Grey300)
            )

            Spacer(modifier = Modifier.width(22.dp))

            // 중간 텍스트
            Text(
                text = "또는",
                style = MaterialTheme.typography.titleSmall,
                color = Grey300
            )

            Spacer(modifier = Modifier.width(22.dp))

            // 오른쪽 실선
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Grey300)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 회원가입 링크
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "아직 회원이 아니신가요?",
                style = MaterialTheme.typography.bodySmall, // 15sp, Regular
                color = Grey600
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "회원가입",
                style = MaterialTheme.typography.titleSmall, // 14sp, Bold
                color = Primary500,
                modifier = Modifier.clickable { onNavigateToSignUp() }
            )
        }
    }
}