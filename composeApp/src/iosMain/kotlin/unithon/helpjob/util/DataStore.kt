package unithon.helpjob.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * iOS DataStore 생성 함수
 * NSDocumentDirectory를 사용하여 파일 저장
 */
@OptIn(ExperimentalForeignApi::class)
fun createDataStore(): DataStore<Preferences> {
    return androidx.datastore.preferences.core.PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            val path = requireNotNull(documentDirectory).path + "/app_prefs.preferences_pb"
            path.toPath()
        }
    )
}
