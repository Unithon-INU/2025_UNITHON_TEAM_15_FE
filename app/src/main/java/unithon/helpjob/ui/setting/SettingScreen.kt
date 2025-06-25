package unithon.helpjob.ui.setting

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import unithon.helpjob.R
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
    onLogoutClick: () -> Unit,
    homeViewModel: HomeViewModel,
    settingViewModel: SettingViewModel = hiltViewModel()
) {
    var showResetDialog by remember { mutableStateOf(false) }
    var isResetting by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    LanguageAwareScreen {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                HelpJobTopAppBar(
                    title = R.string.setting_top_bar_title,
                    onBack = onBack
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(top = 14.dp),
            ) {
                // 설정 섹션
                SettingSectionHeader(
                    title = R.string.setting_section_config,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(19.dp))

                SettingItem(
                    title = R.string.setting_app_language,
                    onClick = onLanguageSettingClick,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                SettingItem(
                    title = R.string.setting_reset_progress,
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
                    title = R.string.setting_section_info,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(19.dp))

                SettingItem(
                    title = R.string.setting_community_guidelines,
                    onClick = { /* 아직 구현하지 않음 */ },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                SettingItem(
                    title = R.string.setting_privacy_policy,
                    onClick = { /* 아직 구현하지 않음 */ },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                SettingItem(
                    title = R.string.setting_inquiry,
                    onClick = { /* 아직 구현하지 않음 */ },
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
                    title = R.string.setting_section_account,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                SettingItem(
                    title = R.string.setting_logout,
                    onClick = onLogoutClick,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                SettingItem(
                    title = R.string.setting_withdrawal,
                    onClick = { /* 아직 구현하지 않음 */ },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 앱 버전
                Text(
                    text = stringResource(R.string.setting_app_version),
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
                        coroutineScope.launch {
                            try {
                                isResetting = true
                                settingViewModel.resetProgress()
                                showResetDialog = false
                                homeViewModel.refresh()
                            } catch (e: Exception) {
                                // TODO: 에러 처리
                            } finally {
                                isResetting = false
                            }
                        }
                    }
                }
            )
        }
    }


}

@Composable
private fun SettingSectionHeader(
    @StringRes title: Int,
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
    @StringRes title: Int,
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
            painter = painterResource(R.drawable.setting_check),
            contentDescription = null,
            tint = Grey700
        )
    }
}