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
import helpjob.composeapp.generated.resources.setting_terms_of_service
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.components.HtmlWebView

@Composable
fun TermsOfServiceScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TermsOfServiceViewModel = koinViewModel()
) {
    var htmlContent by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        htmlContent = viewModel.getTermsOfService()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        HelpJobTopAppBar(
            title = Res.string.setting_terms_of_service,
            onBack = onBack
        )

        HtmlWebView(
            htmlContent = htmlContent,
            modifier = Modifier.fillMaxSize()
        )
    }
}
