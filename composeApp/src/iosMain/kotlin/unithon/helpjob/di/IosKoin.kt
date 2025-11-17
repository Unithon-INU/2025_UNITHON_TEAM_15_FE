package unithon.helpjob.di

import org.koin.core.context.startKoin

/**
 * iOS Koin 초기화 함수
 * iOSApp.swift에서 호출
 */
fun initKoin() {
    startKoin {
        modules(
            // iOS 전용
            iosDataModule,
            iosNetworkModule,
            iosViewModelModule,

            // 공통
            commonDataModule,
            commonNetworkModule
        )
    }
}
