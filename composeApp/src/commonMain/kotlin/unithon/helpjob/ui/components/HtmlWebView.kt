package unithon.helpjob.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 플랫폼별 HTML 콘텐츠 표시 WebView
 */
@Composable
expect fun HtmlWebView(
    htmlContent: String?,
    modifier: Modifier = Modifier
)
