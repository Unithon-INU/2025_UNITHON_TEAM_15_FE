package unithon.helpjob.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import unithon.helpjob.BuildConfig
import unithon.helpjob.data.network.AuthInterceptor
import unithon.helpjob.data.network.HelpJobApiService
import unithon.helpjob.data.repository.*
import unithon.helpjob.ui.auth.nickname.NicknameSetupViewModel
import unithon.helpjob.ui.auth.signin.SignInViewModel
import unithon.helpjob.ui.auth.signup.SignUpViewModel
import unithon.helpjob.ui.document.DocumentViewModel
import unithon.helpjob.ui.main.HomeViewModel
import unithon.helpjob.ui.onboarding.OnboardingViewModel
import unithon.helpjob.ui.profile.ProfileViewModel
import unithon.helpjob.ui.setting.LanguageSettingViewModel
import unithon.helpjob.ui.setting.PrivacyPolicyViewModel
import unithon.helpjob.ui.setting.TermsOfServiceViewModel

import java.util.concurrent.TimeUnit

/**
 * Koin DI 모듈
 * Hilt에서 Koin으로 전환
 */

// 🔹 Data 계층 모듈
val dataModule = module {
    // DataStore
    single<DataStore<Preferences>> { get<Context>().dataStore }

    // AppLocaleManager
    single { AppLocaleManager(androidContext()) }

    // LanguageRepository (단일 구현체)
    single { LanguageRepository(androidContext(), get()) }

    // SignUpDataRepository (단일 구현체)
    single { SignUpDataRepository() }

    // Repository 인터페이스 → 구현체 바인딩
    single<AuthRepository> { DefaultAuthRepository(get(), androidContext()) }
    single<EmploymentCheckRepository> { DefaultEmploymentCheckRepository(get(), get()) }
    single<DocumentRepository> { DefaultDocumentRepository(get(), get()) }
    single<PolicyRepository> { DefaultPolicyRepository(get()) }
}

// 🔹 Network 계층 모듈
val networkModule = module {
    // Json
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    // AuthInterceptor
    single { AuthInterceptor(get()) }

    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit
    @OptIn(ExperimentalSerializationApi::class)
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(get())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    // HelpJobApiService
    single { get<Retrofit>().create(HelpJobApiService::class.java) }
}

// 🔹 ViewModel 모듈
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DocumentViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { NicknameSetupViewModel(get(), get()) }
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { LanguageSettingViewModel(get()) }
    viewModel { TermsOfServiceViewModel(get()) }
    viewModel { PrivacyPolicyViewModel(get()) }
}

/**
 * Koin 초기화 함수
 * HelpJobApplication에서 호출
 */
fun initKoin(context: Context) {
    org.koin.core.context.startKoin {
        androidContext(context)
        modules(
            dataModule,
            networkModule,
            viewModelModule
        )
    }
}