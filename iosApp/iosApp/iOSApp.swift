import SwiftUI
import ComposeApp
import FirebaseCore

@main
struct iOSApp: App {

    init() {
        // Firebase 초기화
        FirebaseApp.configure()

        // Koin 초기화 (Firebase Analytics Wrapper 주입)
        IosKoinKt.doInitKoin(analyticsWrapper: FirebaseAnalyticsManager.shared)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}