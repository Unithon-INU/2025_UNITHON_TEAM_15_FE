package unithon.helpjob.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.setting_privacy_policy
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.components.HtmlWebView

@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PrivacyPolicyViewModel = koinViewModel()
) {
    var htmlContent by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        htmlContent = viewModel.getPrivacyPolicy()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        HelpJobTopAppBar(
            title = Res.string.setting_privacy_policy,
            onBack = onBack
        )

        HtmlWebView(
            htmlContent = htmlContent,
            modifier = Modifier.fillMaxSize()
        )
    }
}
