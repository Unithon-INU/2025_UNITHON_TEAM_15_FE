package unithon.helpjob.data.analytics

interface AnalyticsService {
    fun logEvent(name: String, params: Map<String, Any>? = null)
}