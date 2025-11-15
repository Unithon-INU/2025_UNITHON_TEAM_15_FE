package unithon.helpjob.util

import platform.Foundation.NSBundle

actual object AppConfig {
    actual val API_BASE_URL: String = "https://checkmate.io.kr" // iOS용 기본값
    actual val DEBUG: Boolean = false // iOS용 기본값
    actual val APP_VERSION: String = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "1.0.0"
}
