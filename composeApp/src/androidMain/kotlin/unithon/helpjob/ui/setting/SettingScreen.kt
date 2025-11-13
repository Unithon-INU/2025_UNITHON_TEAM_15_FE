package unithon.helpjob.ui.setting

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.setting_app_language
import helpjob.composeapp.generated.resources.setting_app_version
import helpjob.composeapp.generated.resources.setting_check
import helpjob.composeapp.generated.resources.setting_logout
import helpjob.composeapp.generated.resources.setting_open_source_license
import helpjob.composeapp.generated.resources.setting_privacy_policy
import helpjob.composeapp.generated.resources.setting_reset_progress
import helpjob.composeapp.generated.resources.setting_section_account
import helpjob.composeapp.generated.resources.setting_section_config
import helpjob.composeapp.generated.resources.setting_section_info
import helpjob.composeapp.generated.resources.setting_terms_of_service
import helpjob.composeapp.generated.resources.setting_top_bar_title
import helpjob.composeapp.generated.resources.setting_withdrawal
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.data.repository.LanguageAwareScreen
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.main.HomeViewModel
import unithon.helpjob.ui.setting.components.ResetProgressDialog
import unithon.helpjob.ui.theme.Grey100
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.body4
import unithon.helpjob.ui.theme.subhead1
import unithon.helpjob.util.noRippleClickable

@Composable
fun SettingScreen(
    onBack: () -> Unit,
    onLanguageSettingClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit,
    onLogoutClick: () -> Unit,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    settingViewModel: SettingViewModel = koinViewModel()
) {
    var showResetDialog by remember { mutableStateOf(false) }
    var isResetting by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(settingViewModel.snackbarMessage) {
        settingViewModel.snackbarMessage.collect { messageRes ->
            snackbarHostState.showSnackbar(
                message = getString(messageRes)
            )
        }
    }

    LanguageAwareScreen {
        Column(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            HelpJobTopAppBar(
                title = Res.string.setting_top_bar_title,
                onBack = onBack
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 14.dp),
            ) {
                // 설정 섹션
                SettingSectionHeader(
                    title = Res.string.setting_section_config,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(19.dp))

                SettingItem(
                    title = Res.string.setting_app_language,
                    onClick = onLanguageSettingClick,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                SettingItem(
                    title = Res.string.setting_reset_progress,
                    onClick = { showResetDialog = true },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 4.dp,
                    color = Grey100
                )

                Spacer(modifier = Modifier.height(17.dp))

                // 정보 섹션
                SettingSectionHeader(
                    title = Res.string.setting_section_info,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(19.dp))

                SettingItem(
                    title = Res.string.setting_privacy_policy,
                    onClick = onPrivacyPolicyClick,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                SettingItem(
                    title = Res.string.setting_terms_of_service,
                    onClick = onTermsOfServiceClick,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                SettingItem(
                    title = Res.string.setting_open_source_license,
                    onClick = {
                        context.startActivity(
                            Intent(context, OssLicensesMenuActivity::class.java)
                        )
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 4.dp,
                    color = Grey100
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 계정 섹션
                SettingSectionHeader(
                    title = Res.string.setting_section_account,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                SettingItem(
                    title = Res.string.setting_logout,
                    onClick = {
                        settingViewModel.logout()
                        onLogoutClick()
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                SettingItem(
                    title = Res.string.setting_withdrawal,
                    onClick = { /* 아직 구현하지 않음 */ },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 앱 버전
                Text(
                    text = stringResource(Res.string.setting_app_version),
                    style = MaterialTheme.typography.body4,
                    color = Color(0xFF70737D),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

            }
        }
        if (showResetDialog) {
            ResetProgressDialog(
                onDismiss = {
                    if (!isResetting) {
                        showResetDialog = false
                    }
                },
                onConfirm = {
                    if (!isResetting) {
                        isResetting = true
                        settingViewModel.resetProgress()
                        showResetDialog = false
                        homeViewModel.refresh()
                        isResetting = false
                    }
                }
            )
        }
    }


}

@Composable
private fun SettingSectionHeader(
    title: StringResource,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.body4,
        color = Grey700,
        modifier = modifier
    )
}

@Composable
private fun SettingItem(
    modifier: Modifier = Modifier,
    title: StringResource,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.subhead1,
            lineHeight = 24.sp,
            color = Grey700
        )

        Icon(
            painter = painterResource(Res.drawable.setting_check),
            contentDescription = null,
            tint = Grey700
        )
    }
}