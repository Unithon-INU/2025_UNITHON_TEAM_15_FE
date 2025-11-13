package unithon.helpjob.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import unithon.helpjob.data.network.HelpJobApiService
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.DefaultAuthRepository
import unithon.helpjob.data.repository.DefaultDocumentRepository
import unithon.helpjob.data.repository.DefaultEmploymentCheckRepository
import unithon.helpjob.data.repository.DefaultPolicyRepository
import unithon.helpjob.data.repository.DocumentRepository
import unithon.helpjob.data.repository.EmploymentCheckRepository
import unithon.helpjob.data.repository.HomeStateRepository
import unithon.helpjob.data.repository.PolicyRepository
import unithon.helpjob.data.repository.SignUpDataRepository

/**
 * 공통 Data 계층 모듈 (KMP 공통)
 */
val commonDataModule = module {
    // SignUpDataRepository (단일 구현체)
    single { SignUpDataRepository() }

    // Repository 인터페이스 → 구현체 바인딩
    single<AuthRepository> { DefaultAuthRepository(get(), get()) }
    single<EmploymentCheckRepository> { DefaultEmploymentCheckRepository(get(), get()) }
    single<DocumentRepository> { DefaultDocumentRepository(get(), get()) }
    single<PolicyRepository> { DefaultPolicyRepository(get()) }

    // HomeStateRepository (상태 관리)
    single { HomeStateRepository(get()) }
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
