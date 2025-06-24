package unithon.helpjob.ui.profile.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey600

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