import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        // Koin 초기화
        IosKoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}