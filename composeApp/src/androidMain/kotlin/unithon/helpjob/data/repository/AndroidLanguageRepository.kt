package unithon.helpjob.data.repository

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.snapshotFlow
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber
import unithon.helpjob.data.model.AppLanguage

class AndroidLanguageRepository(
    private val appLocaleManager: AppLocaleManager
) : LanguageRepository {

    // ✅ GlobalLanguageState를 Flow로 변환
    override val currentLanguage: Flow<AppLanguage> =
        snapshotFlow { GlobalLanguageState.currentLanguage.value }
            .distinctUntilChanged()

    override suspend fun setLanguage(language: AppLanguage) {
        Timber.d("🌐 언어 설정 시작: ${language.displayName} (${language.code})")

        try {
            // ✅ GlobalLanguageState만 업데이트 (즉시 반영, Activity 재시작 없음)
            GlobalLanguageState.updateLanguage(language)

            // ✅ DataStore에 저장 (앱 재시작 시 복원용)
            appLocaleManager.saveLanguageToDataStore(language.code)

            // ✅ AppCompatDelegate에 저장 (다음 프로세스 시작 시 Phase 1 동기 읽기용 캐시)
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(language.code)
            )

            Timber.d("✅ 언어 설정 완료: ${language.code}")
        } catch (e: Exception) {
            Timber.e(e, "❌ 언어 설정 실패: ${language.code}")
        }
    }

    override suspend fun getCurrentLanguage(): AppLanguage {
        return appLocaleManager.getCurrentLanguage()
    }
}
