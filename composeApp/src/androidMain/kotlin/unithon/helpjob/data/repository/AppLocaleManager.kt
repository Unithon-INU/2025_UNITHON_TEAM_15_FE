package unithon.helpjob.data.repository

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber
import unithon.helpjob.data.model.AppLanguage
import java.util.Locale


class AppLocaleManager(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val KEY_LANGUAGE_CODE = stringPreferencesKey("language_code")
    }

    /**
     * DataStore에 언어 저장 (시스템 API 호출 없음, Activity 재시작 없음)
     */
    suspend fun saveLanguageToDataStore(languageCode: String) {
        try {
            dataStore.edit { preferences ->
                preferences[KEY_LANGUAGE_CODE] = languageCode
            }
            Timber.d("✅ DataStore에 언어 저장 완료: $languageCode")
        } catch (e: Exception) {
            Timber.e(e, "❌ 언어 저장 실패: $languageCode")
        }
    }

    /**
     * 앱 시작 시 저장된 언어 복원
     */
    suspend fun restoreSavedLanguage() {
        try {
            val savedLanguageCode = dataStore.data
                .map { it[KEY_LANGUAGE_CODE] }
                .firstOrNull()

            if (savedLanguageCode != null) {
                val savedLanguage = AppLanguage.fromCode(savedLanguageCode)
                GlobalLanguageState.updateLanguage(savedLanguage)
                Timber.d("✅ 저장된 언어 복원: ${savedLanguage.displayName}")
            }
        } catch (e: Exception) {
            Timber.e(e, "❌ 언어 복원 실패")
        }
    }

    /**
     * 현재 설정된 언어 코드 가져오기 (DataStore에서 읽기)
     */
    private suspend fun getCurrentLanguageCode(): String {
        return try {
            val savedLanguageCode = dataStore.data
                .map { it[KEY_LANGUAGE_CODE] }
                .firstOrNull()
            Timber.d("✅ DataStore 언어 코드: $savedLanguageCode")
            savedLanguageCode ?: getDefaultLanguageCode()
        } catch (e: Exception) {
            Timber.e(e, "현재 언어 코드 가져오기 실패")
            getDefaultLanguageCode()
        }
    }

    /**
     * 현재 설정된 AppLanguage 가져오기
     */
    suspend fun getCurrentLanguage(): AppLanguage {
        val languageCode = getCurrentLanguageCode()
        return AppLanguage.fromCode(languageCode)
    }

    /**
     * 기본 언어 코드
     */
    private fun getDefaultLanguageCode(): String {
        return AppLanguage.ENGLISH.code
    }

}

@Composable
fun DynamicLanguageProvider(
    currentLanguage: AppLanguage,
    content: @Composable () -> Unit
) {
    val baseContext = LocalContext.current
    val isInPreview = LocalInspectionMode.current

    if (isInPreview) {
        // ✅ 프리뷰 환경에서는 언어 변경 로직을 우회하고 기본 Context 사용
        content()
    } else {
        // baseContext도 key에 추가 → Configuration change 시 languageContext 재생성
        val languageContext = remember(currentLanguage, baseContext) {
            createLanguageContext(baseContext, currentLanguage.code, isInPreview)
        }

        val configuration = remember(currentLanguage, baseContext) {
            Configuration(languageContext.resources.configuration)
        }

        // 매 recomposition마다 Locale.setDefault() 보호
        // WebView, 서드파티 라이브러리가 Locale.setDefault()를 덮어쓰는 경우 복원
        SideEffect {
            val locale = Locale.forLanguageTag(currentLanguage.code)
            if (Locale.getDefault() != locale) {
                Locale.setDefault(locale)
            }
        }

        CompositionLocalProvider(
            LocalContext provides languageContext,
            LocalConfiguration provides configuration
        ) {
            content()
        }
    }
}

private fun createLanguageContext(
    baseContext: Context,
    languageCode: String,
    isInPreview: Boolean = false
): Context {
    val locale = Locale.forLanguageTag(languageCode)

    // ✅ 프리뷰 환경이 아닐 때만 Locale.setDefault() 호출
    if (!isInPreview) {
        try {
            Locale.setDefault(locale) // 전역 기본 Locale 설정
        } catch (e: Exception) {
            // 프리뷰 환경에서는 무시
        }
    }

    val configuration = Configuration(baseContext.resources.configuration)
    configuration.setLocale(locale)

    return baseContext.createConfigurationContext(configuration)
}
