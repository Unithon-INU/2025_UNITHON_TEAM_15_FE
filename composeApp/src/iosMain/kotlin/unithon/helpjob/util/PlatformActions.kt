package unithon.helpjob.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * iOS implementation of [rememberPlatformActions].
 *
 * TODO(Phase 4): Implement iOS-specific open source licenses screen
 *   Options:
 *   1. Web view with generated licenses HTML
 *   2. Native SwiftUI list screen
 *   3. Third-party library (e.g., LicenseList for SwiftUI)
 *
 * For now, this is a stub implementation that prints a message.
 */
@Composable
actual fun rememberPlatformActions(): PlatformActions {
    return remember {
        object : PlatformActions {
            override fun openOssLicenses() {
                // TODO(Phase 4): Navigate to iOS licenses screen
                println("iOS OSS Licenses - Not Implemented")
            }
        }
    }
}
