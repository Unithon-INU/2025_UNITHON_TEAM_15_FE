package unithon.helpjob.util

import unithon.helpjob.BuildConfig

actual object AppConfig {
    actual val API_BASE_URL: String = BuildConfig.API_BASE_URL
    actual val DEBUG: Boolean = BuildConfig.DEBUG
}
