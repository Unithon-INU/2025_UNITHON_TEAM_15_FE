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
import unithon.helpjob.data.model.request.*
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

    override suspend fun signIn(email: String, password: String): TokenResponse {
        val response = apiService.signIn(MemberSignInReq(email, password))
        if (response.isSuccessful) {
            response.body()?.let { tokenResponse ->
                saveToken(tokenResponse.token)
                return tokenResponse
            }
        }
        throw Exception(response.errorBody()?.string() ?: "로그인 실패")
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
        val token = getToken() ?: throw UnauthorizedException()
        val response = apiService.setNickname(
            "Bearer $token",
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
        languageLevel: String,
        visaType: String,
        industry: String
    ): TokenResponse {
        val token = getToken() ?: throw UnauthorizedException()
        val response = apiService.setProfile(
            "Bearer $token",
            MemberProfileReq(language, languageLevel, visaType, industry)
        )
        if (response.isSuccessful) {
            response.body()?.let { tokenResponse ->
                saveToken(tokenResponse.token)
                return tokenResponse
            }
        }
        throw Exception(response.errorBody()?.string() ?: "프로필 설정 실패")
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