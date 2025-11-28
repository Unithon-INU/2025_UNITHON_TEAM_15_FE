package unithon.helpjob.util

import platform.Foundation.NSBundle
import unithon.helpjob.BuildKonfig

actual object AppConfig {
    actual val API_BASE_URL: String = BuildKonfig.API_BASE_URL
    actual val IS_DEBUG: Boolean = BuildKonfig.DEBUG
    actual val APP_VERSION: String = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "1.0.0"
}
