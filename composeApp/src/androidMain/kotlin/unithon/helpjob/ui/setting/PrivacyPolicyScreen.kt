package unithon.helpjob.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.setting_privacy_policy
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.components.HtmlWebView

@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit,
    viewModel: PrivacyPolicyViewModel = koinViewModel()
) {
    val htmlContent by viewModel.htmlContent.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        HelpJobTopAppBar(
            title = Res.string.setting_privacy_policy,
            onBack = onBack
        )

        HtmlWebView(
            htmlContent = htmlContent,
            modifier = Modifier.weight(1f)
        )
    }
}