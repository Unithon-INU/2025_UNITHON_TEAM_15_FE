package unithon.helpjob.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber
import unithon.helpjob.data.model.AppLanguage
import javax.inject.Inject
import javax.inject.Singleton

// 굳이 저장/불러오기만 있는 거 말고 복잡한 로직이 없어서 이건 인터페이스 없이 바로 구현했어요

@Singleton
class LanguageRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appLocaleManager: AppLocaleManager
) {

    suspend fun setLanguage(language: AppLanguage) {
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

//    private fun applyLanguage(language: AppLanguage) {
//        try {
//            AppCompatDelegate.setApplicationLocales(
//                LocaleListCompat.forLanguageTags(language.code)
//            )
//            Timber.d("언어 적용 성공: ${language.code}")
//        } catch (e: Exception) {
//            Timber.e(e, "언어 적용 실패: ${language.code}")
//        }
//    }
}