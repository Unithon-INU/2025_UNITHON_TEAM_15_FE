package unithon.helpjob.util

import unithon.helpjob.HelpJobApplication

/**
 * Android 플랫폼 Analytics 구현
 * Firebase Analytics 사용
 */
actual object Analytics {
    actual fun logEvent(eventName: String, params: Map<String, Any>?) {
        HelpJobApplication.analytics.logEvent(eventName, params)
    }
}
