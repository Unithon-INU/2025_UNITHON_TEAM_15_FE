package unithon.helpjob.data.repository

import timber.log.Timber
import unithon.helpjob.data.model.AppLanguage

// 굳이 저장/불러오기만 있는 거 말고 복잡한 로직이 없어서 이건 인터페이스 없이 바로 구현했어요

class LanguageRepository(
    private val appLocaleManager: AppLocaleManager
) {

    fun setLanguage(language: AppLanguage) {
        Timber.d("🌐 언어 설정 시작: ${language.displayName} (${language.code})")

        try {
            appLocaleManager.changeLanguage(language.code)

            Timber.d("✅ 언어 설정 완료: ${language.code}")
        } catch (e: Exception) {
            Timber.e(e, "❌ 언어 설정 실패: ${language.code}")
        }
    }


    fun getCurrentLanguage(): AppLanguage {
        return appLocaleManager.getCurrentLanguage()
    }

}