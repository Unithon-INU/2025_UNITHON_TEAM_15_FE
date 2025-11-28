package unithon.helpjob

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber
import unithon.helpjob.data.analytics.AnalyticsService
import unithon.helpjob.data.analytics.AndroidAnalyticsService
import unithon.helpjob.data.repository.AppLocaleManager
import unithon.helpjob.data.repository.GlobalLanguageState
import unithon.helpjob.data.repository.LanguageRepository

class HelpJobApplication : Application() {
    private val languageRepository: LanguageRepository by inject()
    private val appLocaleManager: AppLocaleManager by inject()

    companion object {
        lateinit var analytics: AnalyticsService
    }

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        // Koin 초기화 (가장 먼저!)
        unithon.helpjob.di.initKoin(this)

        // UnCaughtExceptionHandler 설정 (최후 방어선)
        setupUncaughtExceptionHandler()
        // 저장된 언어 설정이 있으면 적용, 없으면 시스템 언어 그대로
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
                // ✅ 저장된 언어 복원
                appLocaleManager.restoreSavedLanguage()

                // ✅ 현재 언어 가져오기 (DataStore)
                val savedLanguage = languageRepository.getCurrentLanguage()
                Timber.d("저장된 언어: ${savedLanguage.code}")

                // ✅ GlobalLanguageState 초기화 (UI 반영)
                GlobalLanguageState.initializeLanguage(savedLanguage)
            } catch (e: Exception) {
                Timber.e(e, "Failed to initialize language")
                // 저장된 언어가 없으면 시스템 언어 그대로
            }
        }
    }
}