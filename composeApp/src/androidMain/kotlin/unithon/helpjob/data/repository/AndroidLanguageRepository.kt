package unithon.helpjob.data.repository

import timber.log.Timber
import unithon.helpjob.data.model.AppLanguage

class AndroidLanguageRepository(
    private val appLocaleManager: AppLocaleManager
) : LanguageRepository {

    override fun setLanguage(language: AppLanguage) {
        Timber.d("🌐 언어 설정 시작: ${language.displayName} (${language.code})")

        try {
            appLocaleManager.changeLanguage(language.code)

            Timber.d("✅ 언어 설정 완료: ${language.code}")
        } catch (e: Exception) {
            Timber.e(e, "❌ 언어 설정 실패: ${language.code}")
        }
    }

    override fun getCurrentLanguage(): AppLanguage {
        return appLocaleManager.getCurrentLanguage()
    }
}
