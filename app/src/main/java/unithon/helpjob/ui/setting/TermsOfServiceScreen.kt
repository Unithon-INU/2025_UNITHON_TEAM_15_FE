package unithon.helpjob.ui.setting

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobTopAppBar

@Composable
fun TermsOfServiceScreen(
    onBack: () -> Unit,
    viewModel: TermsOfServiceViewModel = hiltViewModel()
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
            title = R.string.setting_terms_of_service,
            onBack = onBack
        )

        htmlContent?.let { html ->
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = false
                    }
                },
                update = { webView ->
                    webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                }
            )
        }
    }
}