package unithon.helpjob.util

/**
 * 플랫폼별 Analytics 로깅 유틸리티
 */
expect object Analytics {
    /**
     * Analytics 이벤트 로깅
     * @param eventName 이벤트 이름
     * @param params 이벤트 파라미터 (선택적)
     */
    fun logEvent(eventName: String, params: Map<String, Any>? = null)
}
