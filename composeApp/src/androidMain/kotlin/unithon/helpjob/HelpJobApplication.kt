package unithon.helpjob

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber
import unithon.helpjob.data.analytics.AnalyticsService
import unithon.helpjob.data.analytics.AndroidAnalyticsService
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.data.repository.GlobalLanguageState
import unithon.helpjob.data.repository.LanguageRepository

class HelpJobApplication : Application() {
    private val languageRepository: LanguageRepository by inject()

    companion object {
        lateinit var analytics: AnalyticsService
    }

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        // ✅ Phase 1: AppCompatDelegate에서 저장된 언어 동기 읽기
        // - AppLocalesMetadataHolderService(autoStoreLocales=true)가 자동 저장
        // - DataStore(비동기)보다 먼저 실행되어 플래시 방지
        try {
            val savedLocales = AppCompatDelegate.getApplicationLocales()
            if (!savedLocales.isEmpty) {
                val languageTag = savedLocales[0]?.language ?: ""
                if (languageTag.isNotBlank()) {
                    GlobalLanguageState.initializeLanguage(AppLanguage.fromCode(languageTag))
                }
            }
            // empty면 mutableStateOf(ENGLISH) 기본값 유지 (첫 실행 시)
        } catch (e: Exception) {
            Timber.w(e, "Phase 1 언어 초기화 실패, 기본값 ENGLISH 사용")
        }

        // Koin 초기화 (가장 먼저!)
        unithon.helpjob.di.initKoin(this)

        // UnCaughtExceptionHandler 설정 (최후 방어선)
        setupUncaughtExceptionHandler()

        // ✅ Phase 2: DataStore에서 저장된 언어 비동기 로딩 (정확한 값으로 업그레이드)
        initializeLanguage()

        analytics = AndroidAnalyticsService()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun setupUncaughtExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Timber.e(throwable, "Uncaught exception on thread: ${thread.name}")

            // 로그 기록 후 기본 핸들러로 전달
            // 앱을 완전히 크래시시키지 않고 재시작하도록 함
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun initializeLanguage() {
        applicationScope.launch {
            try {
                // ✅ DataStore에서 저장된 언어 읽기 (정확한 값으로 확인)
                val savedLanguage = languageRepository.getCurrentLanguage()
                Timber.d("저장된 언어: ${savedLanguage.code}")

                // ✅ GlobalLanguageState 갱신 (Phase 1과 동일하면 recomposition 없음)
                GlobalLanguageState.initializeLanguage(savedLanguage)

                // ✅ AppCompatDelegate에 저장 (이미 맞는 언어면 호출 안 함 → config change 방지)
                val currentLocales = AppCompatDelegate.getApplicationLocales()
                val currentCode = if (!currentLocales.isEmpty) currentLocales[0]?.language ?: "" else ""
                if (currentCode != savedLanguage.code) {
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags(savedLanguage.code)
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to initialize language")
                // Phase 1에서 설정한 값 유지 (ENGLISH 기본값)
            }
        }
    }
}