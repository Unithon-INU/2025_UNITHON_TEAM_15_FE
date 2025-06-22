package unithon.helpjob.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import unithon.helpjob.R
import unithon.helpjob.ui.profile.components.ProfileTopAppBar
import unithon.helpjob.ui.theme.Grey400
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.PretendardFontFamily

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ProfileTopAppBar(
                onNavigateToSettings = onNavigateToSettings
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(top = 6.dp, start = 20.dp, end = 20.dp)
        ) {
            // 인사말 - 22sp Bold 커스텀 스타일
            Text(
                text = stringResource(
                    id = R.string.profile_greeting,
                    uiState.nickname ?: "유니톤"
                ),
                style = TextStyle(
                    fontSize = 22.sp,
                    lineHeight = 32.sp,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Grey700
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 이메일 정보 Row - 양끝 정렬
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = uiState.email ?: "temp@naver.com",
                    style = MaterialTheme.typography.bodyLarge, // 15sp Bold
                    color = Grey500
                )

                Text(
                    text = stringResource(id = R.string.profile_email_signup_type),
                    style = MaterialTheme.typography.bodyMedium, // 15sp Medium
                    color = Grey400
                )
            }
        }
    }
}