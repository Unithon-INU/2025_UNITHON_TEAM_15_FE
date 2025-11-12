package unithon.helpjob.util

/**
 * iOS 플랫폼 Analytics 구현
 * TODO: Firebase Analytics iOS 연동 필요
 */
actual object Analytics {
    actual fun logEvent(eventName: String, params: Map<String, Any>?) {
        // iOS Firebase Analytics 연동 시 구현
        // 현재는 빈 구현 (로그만 출력)
        println("Analytics [iOS]: $eventName, params: $params")
    }
}
