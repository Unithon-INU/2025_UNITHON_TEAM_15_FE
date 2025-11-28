package unithon.helpjob.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.WebKit.WKWebView

/**
 * iOS WKWebView를 사용한 HTML 콘텐츠 표시
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun HtmlWebView(
    htmlContent: String?,
    modifier: Modifier
) {
    UIKitView(
        modifier = modifier,
        factory = {
            WKWebView()
        },
        update = { webView ->
            htmlContent?.let { html ->
                webView.loadHTMLString(html, baseURL = null)
            }
        }
    )
}
