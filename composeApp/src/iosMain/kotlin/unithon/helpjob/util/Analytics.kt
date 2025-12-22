package unithon.helpjob.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import unithon.helpjob.data.analytics.AnalyticsService

/**
 * iOS 플랫폼 Analytics 구현
 * Koin을 통해 주입받은 AnalyticsService 사용
 */
actual object Analytics : KoinComponent {
    private val analyticsService: AnalyticsService by inject()

    actual fun logEvent(eventName: String, params: Map<String, Any>?) {
        analyticsService.logEvent(eventName, params)
    }
}
