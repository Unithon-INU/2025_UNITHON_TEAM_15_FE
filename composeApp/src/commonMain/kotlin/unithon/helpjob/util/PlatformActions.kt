package unithon.helpjob.util

import androidx.compose.runtime.Composable

/**
 * Platform-specific actions for cross-platform UI interactions.
 *
 * This interface provides platform-specific implementations of common actions
 * that require native platform APIs.
 */
interface PlatformActions {
    /**
     * Opens the Open Source Licenses screen.
     *
     * - Android: Launches OssLicensesMenuActivity
     * - iOS: Opens licenses screen (web view or native)
     */
    fun openOssLicenses()
}

/**
 * Remembers platform-specific actions.
 *
 * This function follows the Compose Multiplatform Resources pattern
 * (similar to Font, SvgShowcase) where platform-specific types
 * (like Android's Context) are only used inside actual implementations.
 *
 * @return Platform-specific implementation of [PlatformActions]
 */
@Composable
expect fun rememberPlatformActions(): PlatformActions
