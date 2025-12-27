package unithon.helpjob.data.analytics

/**
 * iOS Firebase Analytics 구현
 * Swift의 FirebaseAnalyticsWrapper를 통해 Firebase Analytics 호출
 */
class IOSAnalyticsService(
    private val wrapper: FirebaseAnalyticsWrapper
) : AnalyticsService {
    override fun logEvent(name: String, params: Map<String, Any>?) {
        wrapper.logEvent(name, params)
    }
}

/**
 * Swift에서 구현할 Firebase Analytics Wrapper 인터페이스
 * iOSApp.swift에서 구현 필요
 */
interface FirebaseAnalyticsWrapper {
    fun logEvent(name: String, params: Map<String, Any>?)
}