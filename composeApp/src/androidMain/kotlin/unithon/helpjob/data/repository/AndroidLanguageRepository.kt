package unithon.helpjob.data.repository

import androidx.compose.runtime.snapshotFlow
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

    override fun setLanguage(language: AppLanguage) {
        Timber.d("🌐 언어 설정 시작: ${language.displayName} (${language.code})")

        try {
            appLocaleManager.changeLanguage(language.code)
            GlobalLanguageState.updateLanguage(language)  // ✅ GlobalLanguageState 동기화

            Timber.d("✅ 언어 설정 완료: ${language.code}")
        } catch (e: Exception) {
            Timber.e(e, "❌ 언어 설정 실패: ${language.code}")
        }
    }

    override fun getCurrentLanguage(): AppLanguage {
        return appLocaleManager.getCurrentLanguage()
    }
}
