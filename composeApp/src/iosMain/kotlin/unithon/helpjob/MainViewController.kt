package unithon.helpjob

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

/**
 * iOS용 메인 ViewController
 * SwiftUI ContentView에서 호출됨
 */
fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        App()
    }
}
