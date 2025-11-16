package unithon.helpjob.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 플랫폼별 URL 로드 WebView
 * 외부 URL을 직접 로드하는 웹뷰 컴포넌트
 */
@Composable
expect fun UrlWebView(
    url: String,
    modifier: Modifier = Modifier
)
