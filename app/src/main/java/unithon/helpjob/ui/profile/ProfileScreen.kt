package unithon.helpjob.ui.profile

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import unithon.helpjob.R
import unithon.helpjob.ui.profile.components.ProfileTopAppBar
import unithon.helpjob.ui.theme.Grey100
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey400
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.PretendardFontFamily
import unithon.helpjob.ui.theme.body4
import unithon.helpjob.ui.theme.headline2
import unithon.helpjob.ui.theme.title2

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
                    uiState.nickname ?: stringResource(R.string.profile_nickname_default)
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
                    text = uiState.email ?: stringResource(R.string.profile_email_default),
                    style = MaterialTheme.typography.bodyLarge, // 15sp Bold
                    color = Grey500
                )

                Text(
                    text = stringResource(id = R.string.profile_email_signup_type),
                    style = MaterialTheme.typography.bodyMedium, // 15sp Medium
                    color = Grey400
                )
            }

            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Grey100,
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .padding(11.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileInfoColumn(
                        label = stringResource(id = R.string.profile_visa_type),
                        value = uiState.visaType ?: stringResource(id = R.string.profile_visa_default),
                        modifier = Modifier.weight(1f)
                    )

                    VerticalDivider(
                        modifier = Modifier.height(71.dp),
                        thickness = 1.dp,
                        color = Grey300
                    )

                    ProfileInfoColumn(
                        label = stringResource(id = R.string.profile_korean_level),
                        value = uiState.topikLevel ?: stringResource(id = R.string.profile_korean_default),
                        modifier = Modifier.weight(1f)
                    )

                    VerticalDivider(
                        modifier = Modifier.height(71.dp),
                        thickness = 1.dp,
                        color = Grey300
                    )

                    ProfileInfoColumn(
                        label = stringResource(id = R.string.profile_preferred_job),
                        value = uiState.industry ?: stringResource(id = R.string.profile_job_default),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(39.dp))

            Text(
                text = stringResource(id = R.string.profile_documents_title),
                style = MaterialTheme.typography.headline2, // 15sp Bold
                color = Grey700
            )
            Spacer(Modifier.height(5.dp))

        }
    }
}

@Composable
private fun ProfileInfoColumn(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body4,
            color = Grey600
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.title2,
            color = Grey600
        )
    }
}