package unithon.helpjob.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

/**
 * iOS implementation of [rememberPlatformActions].
 */
@Composable
actual fun rememberPlatformActions(): PlatformActions {
    return remember {
        object : PlatformActions {
            override fun openOssLicenses() {
                // iOS에서는 WebView로 라이선스 페이지 열기
                // 현재는 placeholder - 추후 WebView 구현 필요
                println("iOS OSS Licenses - Opening in WebView (Not yet implemented)")

                // 임시로 외부 URL 열기 예시
                // val url = NSURL.URLWithString("https://example.com/licenses")
                // url?.let { UIApplication.sharedApplication.openURL(it) }
            }
        }
    }
}
