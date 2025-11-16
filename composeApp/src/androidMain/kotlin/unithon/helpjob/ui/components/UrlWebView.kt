package unithon.helpjob.ui.components

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun UrlWebView(
    url: String,
    modifier: Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true  // 구글 폼 필수
                settings.domStorageEnabled = true  // 로컬 스토리지 지원
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        }
    )
}
