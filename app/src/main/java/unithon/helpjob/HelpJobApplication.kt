package unithon.helpjob

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import unithon.helpjob.data.repository.GlobalLanguageState
import unithon.helpjob.data.repository.LanguageRepository
import javax.inject.Inject

@HiltAndroidApp
class HelpJobApplication : Application() {

    @Inject
    lateinit var languageRepository: LanguageRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        // UnCaughtExceptionHandler 설정 (최후 방어선)
        setupUncaughtExceptionHandler()

        // 저장된 언어 설정이 있으면 적용, 없으면 시스템 언어 그대로
        initializeLanguage()

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
                val savedLanguage = languageRepository.getCurrentLanguage()
                Timber.d("savedLanguage: ${savedLanguage.code}")
                // 저장된 언어가 있으면 적용
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(savedLanguage.code)
                )

                GlobalLanguageState.initializeLanguage(savedLanguage)
            } catch (e: Exception) {
                Timber.e(e, "Failed to initialize language")
                // 저장된 언어가 없으면 시스템 언어 그대로 (코드 없음)
                // 즉, 처음 설치 시에는 시스템 언어를 따름
            }
        }
    }
}