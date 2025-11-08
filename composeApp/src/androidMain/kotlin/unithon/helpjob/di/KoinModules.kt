package unithon.helpjob.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import unithon.helpjob.BuildConfig
import unithon.helpjob.data.model.response.ErrorResponse
import unithon.helpjob.data.network.ApiConstants
import unithon.helpjob.data.network.HelpJobApiService
import unithon.helpjob.data.repository.AppLocaleManager
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.DefaultAuthRepository
import unithon.helpjob.data.repository.DefaultDocumentRepository
import unithon.helpjob.data.repository.DefaultEmploymentCheckRepository
import unithon.helpjob.data.repository.DefaultPolicyRepository
import unithon.helpjob.data.repository.DocumentRepository
import unithon.helpjob.data.repository.EmailAlreadyInUseException
import unithon.helpjob.data.repository.EmailCodeExpiredException
import unithon.helpjob.data.repository.EmailNotFoundException
import unithon.helpjob.data.repository.EmailVerificationFailedException
import unithon.helpjob.data.repository.EmploymentCheckRepository
import unithon.helpjob.data.repository.LanguageRepository
import unithon.helpjob.data.repository.NicknameDuplicateException
import unithon.helpjob.data.repository.PolicyRepository
import unithon.helpjob.data.repository.SignUpDataRepository
import unithon.helpjob.data.repository.WrongPasswordException
import unithon.helpjob.data.repository.dataStore
import unithon.helpjob.ui.auth.nickname.NicknameSetupViewModel
import unithon.helpjob.ui.auth.signin.SignInViewModel
import unithon.helpjob.ui.auth.signup.SignUpViewModel
import unithon.helpjob.ui.calculator.CalculatorViewModel
import unithon.helpjob.ui.document.DocumentViewModel
import unithon.helpjob.ui.main.HomeViewModel
import unithon.helpjob.ui.onboarding.OnboardingViewModel
import unithon.helpjob.ui.profile.ProfileViewModel
import unithon.helpjob.ui.setting.LanguageSettingViewModel
import unithon.helpjob.ui.setting.PrivacyPolicyViewModel
import unithon.helpjob.ui.setting.SettingViewModel
import unithon.helpjob.ui.setting.TermsOfServiceViewModel
import unithon.helpjob.ui.splash.SplashViewModel

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
    single { LanguageRepository(get()) }

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
    // Json (그대로 유지)
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    // HttpClient (Ktor)
    single {
        val tokenDataStore: DataStore<Preferences> = get()

        HttpClient(OkHttp) {
            // Base URL 설정
            defaultRequest {
                url(BuildConfig.API_BASE_URL)
            }

            // JSON 직렬화
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }

            // 로깅
            install(Logging) {
                logger = Logger.DEFAULT
                level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
            }

            // 🔑 공식 패턴: Auth + Bearer
            install(Auth) {
                bearer {
                    loadTokens {
                        val token = tokenDataStore.data
                            .map { it[stringPreferencesKey("auth_token")] }
                            .firstOrNull()

                        token?.let { BearerTokens(it, "") }
                    }

                    sendWithoutRequest { request ->
                        val noAuthEndpoints = listOf(
                            ApiConstants.SIGN_IN,
                            ApiConstants.SIGN_UP,
                            ApiConstants.EMAIL_SEND,
                            ApiConstants.EMAIL_VERIFY,
                            ApiConstants.PRIVACY_POLICY,
                            ApiConstants.TERMS_OF_SERVICE
                        )

                        noAuthEndpoints.none { request.url.encodedPath.contains(it) }
                    }
                }
            }

            // 🚨 전역 에러 처리 (공식 패턴)
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    val clientException = exception as? ClientRequestException
                        ?: return@handleResponseExceptionWithRequest

                    // Ktor 자동 역직렬화 사용
                    val errorResponse: ErrorResponse? = try {
                        clientException.response.body()
                    } catch (e: Exception) {
                        null
                    }

                    // 에러 코드별 예외 매핑 (백엔드 스펙 기준)
                    when (errorResponse?.code) {
                        "404-1" -> throw EmailNotFoundException()
                        "401-5" -> throw WrongPasswordException()
                        "409-1" -> throw EmailAlreadyInUseException()
                        "409-2" -> throw NicknameDuplicateException()
                        "401-6" -> throw EmailVerificationFailedException()
                        "401-7" -> throw EmailCodeExpiredException()
                        else -> throw Exception(
                            errorResponse?.message ?: "알 수 없는 오류가 발생했습니다."
                        )
                    }
                }
            }
        }
    }

    // HelpJobApiService (Ktor 구현체)
    single { HelpJobApiService(get()) }
}

// 🔹 ViewModel 모듈
val viewModelModule = module {
    viewModel { NicknameSetupViewModel(get(), get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { CalculatorViewModel() }
    viewModel { DocumentViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { LanguageSettingViewModel(get()) }
    viewModel { PrivacyPolicyViewModel(get()) }
    viewModel { SettingViewModel(get(), get()) }
    viewModel { TermsOfServiceViewModel(get()) }
    viewModel { SplashViewModel(get()) }
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