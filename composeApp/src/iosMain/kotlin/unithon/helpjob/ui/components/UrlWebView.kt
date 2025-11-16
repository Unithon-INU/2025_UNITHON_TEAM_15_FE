package unithon.helpjob.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun UrlWebView(
    url: String,
    modifier: Modifier
) {
    UIKitView(
        modifier = modifier,
        factory = {
            WKWebView()  // 기본적으로 JS 활성화됨
        },
        update = { webView ->
            NSURL.URLWithString(url)?.let { nsUrl ->
                val request = NSURLRequest.requestWithURL(nsUrl)
                webView.loadRequest(request)
            }
        }
    )
}
