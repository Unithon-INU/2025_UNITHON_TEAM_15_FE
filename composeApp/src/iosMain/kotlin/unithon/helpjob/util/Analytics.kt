package unithon.helpjob.util

/**
 * iOS 플랫폼 Analytics 구현
 *
 * TODO: Firebase Analytics iOS SDK 연동 시 구현
 * 1. Firebase iOS SDK 추가 (CocoaPods 또는 SPM)
 * 2. GoogleService-Info.plist 추가
 * 3. FirebaseAnalytics.logEvent() 호출
 *
 * 현재는 콘솔 로그만 출력하는 stub 구현
 */
actual object Analytics {
    actual fun logEvent(eventName: String, params: Map<String, Any>?) {
        // iOS Firebase Analytics 연동 예시:
        // FirebaseAnalytics.Analytics.logEvent(
        //     name = eventName,
        //     parameters = params?.mapValues { it.value as NSObject }
        // )

        println("📊 Analytics [iOS]: $eventName")
        params?.forEach { (key, value) ->
            println("   - $key: $value")
        }
    }
}
