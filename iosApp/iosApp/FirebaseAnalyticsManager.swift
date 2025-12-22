import Foundation
import ComposeApp
import FirebaseAnalytics

/// Firebase Analytics Wrapper 구현
/// Kotlin의 FirebaseAnalyticsWrapper 인터페이스를 구현
class FirebaseAnalyticsManager: FirebaseAnalyticsWrapper {

    static let shared = FirebaseAnalyticsManager()

    private init() {
        // Singleton
    }

    /// Analytics 이벤트 로깅
    /// - Parameters:
    ///   - name: 이벤트 이름
    ///   - params: 이벤트 파라미터 (optional)
    func logEvent(name: String, params: [String : Any]?) {
        #if DEBUG
        print("📊 [Firebase Analytics] Event: \(name)")
        if let params = params {
            params.forEach { key, value in
                print("   - \(key): \(value)")
            }
        }
        #endif

        // Firebase Analytics 호출
        Analytics.logEvent(name, parameters: params)
    }
}