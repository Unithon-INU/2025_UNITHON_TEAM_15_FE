package unithon.helpjob.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import timber.log.Timber
import unithon.helpjob.data.model.request.EmailSendReq
import unithon.helpjob.data.model.request.EmailVerifyCodeReq
import unithon.helpjob.data.model.request.MemberNicknameReq
import unithon.helpjob.data.model.request.MemberProfileSetReq
import unithon.helpjob.data.model.request.MemberSignInReq
import unithon.helpjob.data.model.request.MemberSignUpReq
import unithon.helpjob.data.model.response.ErrorResponse
import unithon.helpjob.data.model.response.MemberProfileGetRes
import unithon.helpjob.data.model.response.TokenResponse
import unithon.helpjob.data.network.HelpJobApiService
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class DefaultAuthRepository @Inject constructor(
    private val apiService: HelpJobApiService,
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val tokenKey = stringPreferencesKey("auth_token")
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun signIn(email: String, password: String): TokenResponse {
        val response = apiService.signIn(MemberSignInReq(email, password))
        if (response.isSuccessful) {
            response.body()?.let { tokenResponse ->
                saveToken(tokenResponse.token)
                return tokenResponse
            }
        }

        val errorBody = response.errorBody()?.string()

        // 서버 에러 응답 JSON 파싱 시도
        val errorResponse = try {
            if (errorBody?.startsWith("{") == true) {
                json.decodeFromString<ErrorResponse>(errorBody)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }

        when (errorResponse?.code) {
            "404-1" -> throw EmailNotFoundException()
            "401-5" -> throw WrongPasswordException()
            else -> throw Exception(errorBody ?: "로그인 실패")
        }
    }

    override suspend fun signUp(email: String, password: String): TokenResponse {
        val response = apiService.signUp(MemberSignUpReq(email, password))
        if (response.isSuccessful) {
            response.body()?.let { tokenResponse ->
                saveToken(tokenResponse.token)
                return tokenResponse
            }
        }
        throw Exception(response.errorBody()?.string() ?: "회원가입 실패")
    }

    override suspend fun setNickname(nickname: String) {
        val response = apiService.setNickname(
            MemberNicknameReq(nickname)
        )

        if (response.isSuccessful) {
            return
        } else {
            when (response.code()) {
                409 -> throw NicknameDuplicateException()  // CONFLICT
                401 -> throw UnauthorizedException()
                else -> {
                    val errorBody = response.errorBody()?.string()
                    throw Exception(errorBody ?: "닉네임 설정 실패")
                }
            }
        }
    }

    override suspend fun setProfile(
        language: String,
        topikLevel: String,
        visaType: String,
        industry: String
    ) {

        Timber.d("MemberProfileReq ${MemberProfileSetReq(language, topikLevel, visaType, industry)}")
        val response = apiService.setProfile(
            MemberProfileSetReq(language, topikLevel, visaType, industry)
        )
        if (response.isSuccessful) {
            return
        } else {
            when (response.code()) {
                403 -> throw UnauthorizedException()
                else -> {
                    val errorBody = response.errorBody()?.string()
                    throw Exception(errorBody ?: "프로필 설정 실패")
                }
            }
        }
    }

    override suspend fun getMemberProfile(): MemberProfileGetRes {
        val response = apiService.getMemberProfile()

        if (response.isSuccessful) {
            response.body()?.let { profileResponse ->
                return profileResponse
            }
        }

        when (response.code()) {
            401 -> throw UnauthorizedException()
            else -> {
                val errorBody = response.errorBody()?.string()
                throw Exception(errorBody ?: "프로필 조회 실패")
            }
        }
    }

    // 🆕 이메일 인증 관련 구현
    override suspend fun sendEmailVerification(email: String) {
        val response = apiService.sendEmailVerification(EmailSendReq(email))

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()

            // 서버 에러 응답 JSON 파싱 시도
            val errorResponse = try {
                if (errorBody?.startsWith("{") == true) {
                    json.decodeFromString<ErrorResponse>(errorBody)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }

            when (errorResponse?.code) {
                "409-1" -> throw EmailAlreadyInUseException()
                else -> throw Exception(errorBody ?: "이메일 전송 실패")
            }
        }
    }

    override suspend fun verifyEmailCode(email: String, code: String) {
        val response = apiService.verifyEmailCode(EmailVerifyCodeReq(email, code))

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()

            // 서버 에러 응답 JSON 파싱 시도
            val errorResponse = try {
                if (errorBody?.startsWith("{") == true) {
                    json.decodeFromString<ErrorResponse>(errorBody)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }

            when (errorResponse?.code) {
                "401-7" -> throw EmailCodeExpiredException()
                "401-6" -> throw EmailVerificationFailedException()
                else -> throw EmailVerificationFailedException()
            }
        }
    }

    override suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    override suspend fun getToken(): String? {
        return context.dataStore.data
            .map { preferences -> preferences[tokenKey] }
            .firstOrNull()
    }

    override suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(tokenKey)
        }
    }
}