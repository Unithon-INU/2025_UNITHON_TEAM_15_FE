package unithon.helpjob.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber
import unithon.helpjob.data.model.request.EmailSendReq
import unithon.helpjob.data.model.request.EmailVerifyCodeReq
import unithon.helpjob.data.model.request.MemberNicknameReq
import unithon.helpjob.data.model.request.MemberProfileSetReq
import unithon.helpjob.data.model.request.MemberSignInReq
import unithon.helpjob.data.model.request.MemberSignUpReq
import unithon.helpjob.data.model.response.MemberProfileGetRes
import unithon.helpjob.data.model.response.TokenResponse
import unithon.helpjob.data.network.HelpJobApiService

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class DefaultAuthRepository(
    private val apiService: HelpJobApiService,
    private val context: Context
) : AuthRepository {

    private val tokenKey = stringPreferencesKey("auth_token")

    override suspend fun signIn(email: String, password: String): TokenResponse {
        val tokenResponse = apiService.signIn(MemberSignInReq(email, password))
        saveToken(tokenResponse.token)
        return tokenResponse
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    override suspend fun signUp(email: String, password: String): TokenResponse {
        val tokenResponse = apiService.signUp(MemberSignUpReq(email, password))
        saveToken(tokenResponse.token)
        return tokenResponse
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    override suspend fun setNickname(nickname: String) {
        apiService.setNickname(MemberNicknameReq(nickname))
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    override suspend fun setProfile(
        language: String,
        topikLevel: String,
        visaType: String,
        industry: String
    ) {
        Timber.d("MemberProfileReq ${MemberProfileSetReq(language, topikLevel, visaType, industry)}")
        apiService.setProfile(MemberProfileSetReq(language, topikLevel, visaType, industry))
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    override suspend fun getMemberProfile(): MemberProfileGetRes {
        return apiService.getMemberProfile()
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    // 🆕 이메일 인증 관련 구현
    override suspend fun sendEmailVerification(email: String) {
        apiService.sendEmailVerification(EmailSendReq(email))
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    override suspend fun verifyEmailCode(email: String, code: String) {
        apiService.verifyEmailCode(EmailVerifyCodeReq(email, code))
        // ✅ HttpResponseValidator가 자동으로 에러 처리
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

    // 🆕 온보딩 완료 여부 체크 구현
    override suspend fun isOnboardingCompleted(): Boolean {
        return try {
            val profile = getMemberProfile()
            profile.language.isNotEmpty() &&
                    profile.visaType.isNotEmpty() &&
                    profile.topikLevel.isNotEmpty() &&
                    profile.industry.isNotEmpty()
        } catch (e: Exception) {
            false // 프로필 조회 실패 시 온보딩 미완료로 처리
        }
    }
}