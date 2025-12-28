package unithon.helpjob.di

import kotlinx.serialization.json.Json
import org.koin.dsl.binds
import org.koin.dsl.module
import unithon.helpjob.data.network.HelpJobApiService
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.CacheableRepository
import unithon.helpjob.data.repository.DefaultAuthRepository
import unithon.helpjob.data.repository.DefaultDocumentRepository
import unithon.helpjob.data.repository.DefaultEmploymentCheckRepository
import unithon.helpjob.data.repository.DefaultPolicyRepository
import unithon.helpjob.data.repository.DocumentRepository
import unithon.helpjob.data.repository.EmploymentCheckRepository
import unithon.helpjob.data.repository.GuestMockDataSource
import unithon.helpjob.data.repository.HomeStateRepository
import unithon.helpjob.data.repository.PolicyRepository
import unithon.helpjob.data.repository.SignUpDataRepository

/**
 * 공통 Data 계층 모듈 (KMP 공통)
 */
val commonDataModule = module {
    // SignUpDataRepository (단일 구현체)
    single { SignUpDataRepository() }

    // 🆕 Guest Mode 관련
    single { GuestMockDataSource(get()) }

    // Repository 인터페이스 → 구현체 바인딩
    single<AuthRepository> { DefaultAuthRepository(get(), get(), getKoin()) }
    single<EmploymentCheckRepository> {
        DefaultEmploymentCheckRepository(
            apiService = get(),
            languageRepository = get(),
            authRepository = get(),  // 🆕 Guest Mode 분기용
            guestDataSource = get()  // 🆕 Guest Mock Data
        )
    }
    single<DocumentRepository> { DefaultDocumentRepository(get(), get()) }
    single<PolicyRepository> { DefaultPolicyRepository(get()) }

    // HomeStateRepository (상태 관리 + 캐시 관리)
    single { HomeStateRepository(get()) } binds arrayOf(
        HomeStateRepository::class,
        CacheableRepository::class
    )
}

/**
 * 공통 Network 계층 모듈 (KMP 공통)
 */
val commonNetworkModule = module {
    // Json 설정
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    // HelpJobApiService (Ktor 구현체)
    single { HelpJobApiService(get()) }
}
