package unithon.helpjob.util

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

/**
 * Android implementation of [rememberPlatformActions].
 *
 * This follows the Compose Multiplatform Resources pattern where
 * platform-specific types (LocalContext) are only used inside actual implementations.
 *
 * Similar to how Font resources work in Compose Multiplatform:
 * ```
 * @Composable
 * actual fun Font(...): Font {
 *     val context = LocalContext.current
 *     // Android-specific implementation
 * }
 * ```
 */
@Composable
actual fun rememberPlatformActions(): PlatformActions {
    val context = LocalContext.current

    return remember(context) {
        object : PlatformActions {
            override fun openOssLicenses() {
                context.startActivity(
                    Intent(context, OssLicensesMenuActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
            }
        }
    }
}
