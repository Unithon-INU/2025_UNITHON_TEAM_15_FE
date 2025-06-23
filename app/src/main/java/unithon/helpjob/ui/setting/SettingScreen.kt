package unithon.helpjob.ui.setting

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.theme.Grey400
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.util.noRippleClickable

@Composable
fun SettingScreen(
    onBack: () -> Unit,
    onLanguageSettingClick: () -> Unit,
    onResetProgressClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                .padding(top = 38.dp, start = 20.dp, end = 20.dp),
        ) {
            // 설정 섹션
            SettingSectionHeader(title = R.string.setting_section_config)

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = R.string.setting_app_language,
                onClick = onLanguageSettingClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingItem(
                title = R.string.setting_reset_progress,
                onClick = onResetProgressClick
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 정보 섹션
            SettingSectionHeader(title = R.string.setting_section_info)

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = R.string.setting_community_guidelines,
                onClick = { /* 아직 구현하지 않음 */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingItem(
                title = R.string.setting_privacy_policy,
                onClick = { /* 아직 구현하지 않음 */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingItem(
                title = R.string.setting_inquiry,
                onClick = { /* 아직 구현하지 않음 */ },
                titleColor = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 계정 섹션
            SettingSectionHeader(title = R.string.setting_section_account)

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                title = R.string.setting_logout,
                onClick = onLogoutClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingItem(
                title = R.string.setting_withdrawal,
                onClick = { /* 아직 구현하지 않음 */ }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 앱 버전
            Text(
                text = stringResource(R.string.setting_app_version),
                style = MaterialTheme.typography.bodyMedium,
                color = Grey400,
                modifier = Modifier.padding(vertical = 8.dp)
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
        style = MaterialTheme.typography.titleMedium,
        color = Grey700,
        modifier = modifier
    )
}

@Composable
private fun SettingItem(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    onClick: () -> Unit,
    titleColor: Color = Grey700,
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
            style = MaterialTheme.typography.bodyLarge,
            color = titleColor
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = null,
            tint = Grey400,
            modifier = Modifier
                .size(20.dp)
                .rotate(90f) // 오른쪽 화살표로 회전
        )
    }
}