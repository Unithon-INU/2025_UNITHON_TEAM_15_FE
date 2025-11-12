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
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.resources.MR
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.components.HtmlWebView

@Composable
fun TermsOfServiceScreen(
    onBack: () -> Unit,
    viewModel: TermsOfServiceViewModel = koinViewModel()
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
            title = MR.strings.setting_terms_of_service,
            onBack = onBack
        )

        HtmlWebView(
            htmlContent = htmlContent,
            modifier = Modifier.weight(1f)
        )
    }
}