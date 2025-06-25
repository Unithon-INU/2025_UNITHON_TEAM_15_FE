package unithon.helpjob.data.repository

import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.ui.setting.LanguageSettingViewModel
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLocaleManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * 🔥 새로운 접근: LocaleManager + Configuration 직접 업데이트
     */
    fun changeLanguage(languageCode: String) {
        Timber.d("🌐 언어 변경 시작: $languageCode (API ${Build.VERSION.SDK_INT})")

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // 1️⃣ LocaleManager로 시스템 설정 저장
                val localeManager = context.getSystemService(LocaleManager::class.java)
                localeManager.applicationLocales = LocaleList.forLanguageTags(languageCode)
                Timber.d("✅ LocaleManager로 시스템 설정 저장: $languageCode")

                // 2️⃣ 🆕 Configuration 직접 업데이트 (즉시 적용)
                updateContextConfiguration(languageCode)

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
     * 🆕 Context Configuration 직접 업데이트
     */
    private fun updateContextConfiguration(languageCode: String) {
        try {
            val locale = Locale.forLanguageTag(languageCode)
            Locale.setDefault(locale)

            val resources = context.resources
            val configuration = Configuration(resources.configuration)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocales(LocaleList(locale))
            } else {
                @Suppress("DEPRECATION")
                configuration.locale = locale
            }

            // Configuration 업데이트 적용
            resources.updateConfiguration(configuration, resources.displayMetrics)

            Timber.d("✅ Configuration 직접 업데이트 완료: $languageCode")
        } catch (e: Exception) {
            Timber.e(e, "❌ Configuration 업데이트 실패: $languageCode")
        }
    }

    /**
     * 현재 설정된 언어 코드 가져오기
     */
    fun getCurrentLanguageCode(): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+: LocaleManager 우선
                val localeManager = context.getSystemService(LocaleManager::class.java)
                val applicationLocales = localeManager?.applicationLocales

                if (applicationLocales != null && !applicationLocales.isEmpty) {
                    val languageCode = applicationLocales.get(0)?.language
                    Timber.d("LocaleManager 언어 코드: $languageCode")
                    languageCode ?: getDefaultLanguageCode()
                } else {
                    getDefaultLanguageCode()
                }
            } else {
                // Android 12 이하: AppCompatDelegate
                val appCompatLocale = AppCompatDelegate.getApplicationLocales().get(0)
                val languageCode = appCompatLocale?.language
                Timber.d("AppCompat 언어 코드: $languageCode")
                languageCode ?: getDefaultLanguageCode()
            }
        } catch (e: Exception) {
            Timber.e(e, "현재 언어 코드 가져오기 실패")
            getDefaultLanguageCode()
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
        return AppLanguage.ENGLISH.code
    }

}



@Composable
fun DynamicLanguageProvider(
    currentLanguage: AppLanguage,
    content: @Composable () -> Unit
) {
    val baseContext = LocalContext.current

    // 🔥 핵심: 언어별로 새로운 Context 생성
    val languageContext = remember(currentLanguage) {
        createLanguageContext(baseContext, currentLanguage.code)
    }

    // 🔥 추가: Configuration 변경을 강제로 감지시키기
    val configuration = remember(currentLanguage) {
        Configuration(languageContext.resources.configuration)
    }

    // 새로운 Context로 Composition 제공
    CompositionLocalProvider(
        LocalContext provides languageContext,
        LocalConfiguration provides configuration
    ) {
        content()
    }
}

private fun createLanguageContext(baseContext: Context, languageCode: String): Context {
    val locale = Locale.forLanguageTag(languageCode)
    Locale.setDefault(locale) // 전역 기본 Locale 설정

    val configuration = Configuration(baseContext.resources.configuration)
    configuration.setLocale(locale)

    return baseContext.createConfigurationContext(configuration)
}

@Composable
fun LanguageAwareScreen(
    content: @Composable () -> Unit
) {
    val currentLanguage by GlobalLanguageState.currentLanguage

    // 🔥 DynamicLanguageProvider로 감싸기
    DynamicLanguageProvider(currentLanguage = currentLanguage) {
        content()
    }
}

object GlobalLanguageState {
    private val _currentLanguage = mutableStateOf(AppLanguage.ENGLISH)
    val currentLanguage: State<AppLanguage> = _currentLanguage

    fun updateLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        Timber.d("🌐 전역 언어 상태 업데이트: ${language.displayName}")
    }

    fun initializeLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        Timber.d("🌐 전역 언어 초기화: ${language.displayName}")
    }
}