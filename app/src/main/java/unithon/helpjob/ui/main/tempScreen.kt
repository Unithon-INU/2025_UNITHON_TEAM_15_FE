package unithon.helpjob.ui.main

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
import androidx.compose.ui.unit.dp
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.theme.Grey700

@Composable
fun TempScreen(
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "로그인 성공!",
            style = MaterialTheme.typography.headlineLarge,
            color = Grey700
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "임시 메인 화면입니다",
            style = MaterialTheme.typography.bodyMedium,
            color = Grey700
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 로그아웃 버튼
        HelpJobButton(
            text = "로그아웃",
            onClick = { onNavigateToSignIn() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}