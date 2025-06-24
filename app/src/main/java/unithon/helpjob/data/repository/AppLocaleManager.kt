package unithon.helpjob.data.repository

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import unithon.helpjob.data.model.AppLanguage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLocaleManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * 언어 변경 - API 레벨에 따라 다른 방식 사용
     */
    fun changeLanguage(languageCode: String) {
        Timber.d("🌐 언어 변경 시작: $languageCode (API ${Build.VERSION.SDK_INT})")

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // 🆕 Android 13 이상: LocaleManager 사용
                val localeManager = context.getSystemService(LocaleManager::class.java)
                localeManager.applicationLocales = LocaleList.forLanguageTags(languageCode)
                Timber.d("✅ LocaleManager로 언어 변경 완료: $languageCode")
            } else {
                // Android 12 이하: AppCompatDelegate 사용
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(languageCode)
                )
                Timber.d("✅ AppCompatDelegate로 언어 변경 완료: $languageCode")
            }
        } catch (e: Exception) {
            Timber.e(e, "❌ 언어 변경 실패: $languageCode")
        }
    }

    /**
     * 현재 설정된 언어 코드 가져오기
     */
    /**
     * 현재 설정된 언어 코드 가져오기 (실제 UI에서 사용되는 언어)
     */
    fun getCurrentLanguageCode(): String {
        return try {
            // 1️⃣ 실제 Context에서 사용되는 언어 (UI가 렌더링되는 언어)
            val contextLocale = context.resources.configuration.locales[0]
            val contextLanguageCode = contextLocale.language
            Timber.d("Context 언어 코드: $contextLanguageCode")

            // 2️⃣ AppCompatDelegate에서 설정된 언어
            val appCompatLocale = AppCompatDelegate.getApplicationLocales().get(0)
            val appCompatLanguageCode = appCompatLocale?.language
            Timber.d("AppCompat 언어 코드: $appCompatLanguageCode")

            // 3️⃣ Android 13+ LocaleManager에서 설정된 언어
            val localeManagerLanguageCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val locale = context.getSystemService(LocaleManager::class.java)
                    ?.applicationLocales
                    ?.get(0)
                locale?.language
            } else {
                null
            }
            Timber.d("LocaleManager 언어 코드: $localeManagerLanguageCode")

            // 4️⃣ 우선순위: Context > AppCompat > LocaleManager > 기본값
            val finalLanguageCode = when {
                contextLanguageCode.isNotBlank() -> contextLanguageCode
                !appCompatLanguageCode.isNullOrBlank() -> appCompatLanguageCode
                !localeManagerLanguageCode.isNullOrBlank() -> localeManagerLanguageCode
                else -> getCurrentLanguage().code
            }

            Timber.d("최종 선택된 언어 코드: $finalLanguageCode")
            finalLanguageCode
        } catch (e: Exception) {
            Timber.e(e, "현재 언어 코드 가져오기 실패")
            getCurrentLanguage().code
        }
    }

    /**
     * 현재 설정된 AppLanguage 가져오기
     */
    fun getCurrentLanguage(): AppLanguage {
        val languageCode = getCurrentLanguageCode()
        return AppLanguage.fromCode(languageCode)
    }

    /**
     * 기본 언어 코드
     */
    private fun getDefaultLanguageCode(): String {
        return AppLanguage.ENGLISH.code // "en"
    }
}