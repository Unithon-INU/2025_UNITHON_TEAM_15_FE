package unithon.helpjob.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.api.createClientPlugin
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
import timber.log.Timber
import unithon.helpjob.data.model.response.ErrorResponse
import unithon.helpjob.data.network.ApiConstants
import unithon.helpjob.data.repository.AndroidLanguageRepository
import unithon.helpjob.data.repository.AppLocaleManager
import unithon.helpjob.data.repository.EmailAlreadyInUseException
import unithon.helpjob.data.repository.EmailCodeExpiredException
import unithon.helpjob.data.repository.EmailNotFoundException
import unithon.helpjob.data.repository.EmailVerificationFailedException
import unithon.helpjob.data.repository.LanguageRepository
import unithon.helpjob.data.repository.NicknameDuplicateException
import unithon.helpjob.data.repository.WrongPasswordException
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
import unithon.helpjob.util.AppConfig

// DataStore extension (Android 전용)
// 인증 토큰, 언어 설정 등 앱 전반적인 설정 저장
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

/**
 * Android 플랫폼 Data 계층 모듈
 */
val androidDataModule = module {
    // DataStore (Android 전용 생성)
    single<DataStore<Preferences>> { get<Context>().dataStore }

    // AppLocaleManager (Android 전용, DataStore 주입)
    single { AppLocaleManager(get()) }

    // LanguageRepository (Android 구현체)
    single<LanguageRepository> { AndroidLanguageRepository(get()) }
}

/**
 * Android 플랫폼 Network 계층 모듈
 */
val androidNetworkModule = module {
    // HttpClient (Ktor + OkHttp 엔진)
    single {
        val tokenDataStore: DataStore<Preferences> = get()

        // 🔑 커스텀 인증 플러그인: 매 요청마다 DataStore에서 최신 토큰 읽기
        val TokenAuthPlugin = createClientPlugin("TokenAuth") {
            onRequest { request, _ ->
                val noAuthEndpoints = listOf(
                    ApiConstants.SIGN_IN,
                    ApiConstants.SIGN_UP,
                    ApiConstants.EMAIL_SEND,
                    ApiConstants.EMAIL_VERIFY,
                    ApiConstants.PRIVACY_POLICY,
                    ApiConstants.TERMS_OF_SERVICE
                )

                val requiresAuth = noAuthEndpoints.none { request.url.encodedPath.contains(it) }
                Timber.d("[TokenAuth] ${request.url.encodedPath} - 인증 필요: $requiresAuth")

                if (requiresAuth) {
                    // 매 요청마다 DataStore에서 최신 토큰 읽기
                    val token = tokenDataStore.data
                        .map { it[stringPreferencesKey("auth_token")] }
                        .firstOrNull()

                    if (!token.isNullOrBlank()) {
                        request.headers.append("Authorization", "Bearer $token")
                    }
                }
            }
        }

        HttpClient(OkHttp) {
            // 비정상 응답(4xx, 5xx)을 예외로 처리 (Ktor 공식 베스트 프랙티스)
            expectSuccess = true

            // Base URL 설정
            defaultRequest {
                url(AppConfig.API_BASE_URL)
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
                level = if (AppConfig.IS_DEBUG) LogLevel.ALL else LogLevel.NONE
            }

            // 🔑 커스텀 인증 플러그인 적용
            install(TokenAuthPlugin)

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
                        else -> {
                            val status = clientException.response.status.value
                            val message = errorResponse?.message
                                ?: "알 수 없는 오류 (HTTP $status)"
                            throw Exception(message, clientException)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Android ViewModel 모듈
 */
val androidViewModelModule = module {
    viewModel { NicknameSetupViewModel(get(), get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { CalculatorViewModel() }
    viewModel { DocumentViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { LanguageSettingViewModel(get()) }
    viewModel { PrivacyPolicyViewModel(get()) }
    viewModel { SettingViewModel(get(), get(), get()) }
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
            // Android 전용
            androidDataModule,
            androidNetworkModule,
            androidViewModelModule,

            // 공통
            commonDataModule,
            commonNetworkModule
        )
    }
}
