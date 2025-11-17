package unithon.helpjob.data.repository

import androidx.compose.runtime.snapshotFlow
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import unithon.helpjob.data.model.AppLanguage

/**
 * iOS 플랫폼 LanguageRepository 구현
 * DataStore를 사용한 언어 설정 저장/복원
 */
class IosLanguageRepository(
    private val dataStore: DataStore<Preferences>
) : LanguageRepository {

    companion object {
        private val KEY_LANGUAGE_CODE = stringPreferencesKey("language_code")
    }

    // ✅ GlobalLanguageState를 Flow로 변환
    override val currentLanguage: Flow<AppLanguage> =
        snapshotFlow { GlobalLanguageState.currentLanguage.value }
            .distinctUntilChanged()

    override suspend fun setLanguage(language: AppLanguage) {
        println("🌐 [iOS] 언어 설정 시작: ${language.displayName} (${language.code})")

        // ✅ GlobalLanguageState 즉시 업데이트 (UI 즉시 반영)
        GlobalLanguageState.updateLanguage(language)
        println("✅ [iOS] GlobalLanguageState 업데이트 완료: ${language.code}")

        try {
            // ✅ DataStore에 저장 (앱 재시작 시 복원용)
            dataStore.edit { preferences ->
                preferences[KEY_LANGUAGE_CODE] = language.code
            }

            println("✅ [iOS] DataStore 저장 완료: ${language.code}")
        } catch (e: Exception) {
            println("❌ [iOS] DataStore 저장 실패: ${language.code}, error: ${e.message}")
            // GlobalLanguageState는 이미 업데이트 되었으므로 UI는 변경된 상태 유지
        }
    }

    override suspend fun getCurrentLanguage(): AppLanguage {
        return try {
            val savedLanguageCode = dataStore.data
                .map { it[KEY_LANGUAGE_CODE] }
                .firstOrNull()

            if (savedLanguageCode != null) {
                AppLanguage.fromCode(savedLanguageCode)
            } else {
                AppLanguage.ENGLISH // 기본값
            }
        } catch (e: Exception) {
            println("❌ [iOS] 언어 가져오기 실패, 기본값 사용: ${e.message}")
            AppLanguage.ENGLISH
        }
    }

    /**
     * 앱 시작 시 저장된 언어 복원
     */
    suspend fun restoreSavedLanguage() {
        try {
            val savedLanguage = getCurrentLanguage()
            GlobalLanguageState.updateLanguage(savedLanguage)
            println("✅ [iOS] 저장된 언어 복원: ${savedLanguage.displayName}")
        } catch (e: Exception) {
            println("❌ [iOS] 언어 복원 실패: ${e.message}")
        }
    }
}
