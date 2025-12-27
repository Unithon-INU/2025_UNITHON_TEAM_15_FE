package unithon.helpjob.di

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import unithon.helpjob.data.analytics.FirebaseAnalyticsWrapper
import unithon.helpjob.data.repository.IosLanguageRepository
import unithon.helpjob.data.repository.LanguageRepository

/**
 * iOS Koin 초기화 함수
 * iOSApp.swift에서 호출
 * @param analyticsWrapper Swift에서 구현한 Firebase Analytics Wrapper
 */
@OptIn(DelicateCoroutinesApi::class)
fun initKoin(analyticsWrapper: FirebaseAnalyticsWrapper) {
    val koinApp = startKoin {
        modules(
            // iOS 전용
            iosDataModule,
            iosNetworkModule,
            iosViewModelModule,
            // Analytics 모듈 (Swift wrapper 주입)
            createIosAnalyticsModule(analyticsWrapper),

            // 공통
            commonDataModule,
            commonNetworkModule
        )
    }

    // ✅ 앱 시작 시 저장된 언어 설정 복원
    GlobalScope.launch {
        try {
            val languageRepository = koinApp.koin.get<LanguageRepository>()
            if (languageRepository is IosLanguageRepository) {
                languageRepository.restoreSavedLanguage()
                println("✅ [Koin] 저장된 언어 설정 복원 완료")
            }
        } catch (e: Exception) {
            println("❌ [Koin] 언어 설정 복원 실패: ${e.message}")
        }
    }
}
