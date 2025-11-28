package unithon.helpjob.util

import unithon.helpjob.BuildConfig
import unithon.helpjob.BuildKonfig

actual object AppConfig {
    actual val API_BASE_URL: String = BuildKonfig.API_BASE_URL
    actual val IS_DEBUG: Boolean = BuildKonfig.DEBUG
    actual val APP_VERSION: String = BuildConfig.VERSION_NAME
}
