package unithon.helpjob.ui.profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import timber.log.Timber
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onNavigateToSettings: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
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

            HelpJobButton(
                text = "로그아웃",
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppBar(
    onNavigateToSettings: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text("") },
        actions = {
            IconButton(onClick = onNavigateToSettings) {
                Icon(
                    painter = painterResource(id = R.drawable.setting_icon),
                    contentDescription = stringResource(id = R.string.settings_button)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Grey000,
            actionIconContentColor = Grey600
        ),
        windowInsets = WindowInsets(0.dp)
    )
}