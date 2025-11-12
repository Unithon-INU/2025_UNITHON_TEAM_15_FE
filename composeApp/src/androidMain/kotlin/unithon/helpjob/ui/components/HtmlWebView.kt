package unithon.helpjob.ui.components

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Android WebView를 사용한 HTML 콘텐츠 표시
 */
@Composable
actual fun HtmlWebView(
    htmlContent: String?,
    modifier: Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = false
            }
        },
        update = { webView ->
            // htmlContent가 null이 아닐 때만 로드
            htmlContent?.let { html ->
                webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
            }
        }
    )
}
