package unithon.helpjob.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.Koin
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.data.model.GuestChecklist
import unithon.helpjob.data.model.GuestProfile
import unithon.helpjob.util.Logger
import unithon.helpjob.data.model.request.EmailSendReq
import unithon.helpjob.data.model.request.EmailVerifyCodeReq
import unithon.helpjob.data.model.request.MemberNicknameReq
import unithon.helpjob.data.model.request.MemberProfileSetReq
import unithon.helpjob.data.model.request.MemberSignInReq
import unithon.helpjob.data.model.request.MemberSignUpReq
import unithon.helpjob.data.model.response.MemberProfileGetRes
import unithon.helpjob.data.model.response.TokenResponse
import unithon.helpjob.data.network.HelpJobApiService

class DefaultAuthRepository(
    private val apiService: HelpJobApiService,
    private val dataStore: DataStore<Preferences>,
    private val koin: Koin
) : AuthRepository {

    private val tokenKey = stringPreferencesKey("auth_token")

    // 🆕 Guest Mode 관련 키
    private val guestModeKey = booleanPreferencesKey("is_guest_mode")
    private val guestProfileKey = stringPreferencesKey("guest_profile")
    private val guestChecklistKey = stringPreferencesKey("guest_checklist")

    override suspend fun signIn(email: String, password: String): TokenResponse {
        println("🔥 [Auth] 로그인 시도: $email")
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
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
        println("🔥 [Auth] 토큰 저장 완료")
    }

    override suspend fun getToken(): String? {
        return dataStore.data
            .map { preferences -> preferences[tokenKey] }
            .firstOrNull()
    }

    override suspend fun clearToken() {
        println("🔥 [DefaultAuthRepository] clearToken() 시작")

        // 1. DataStore의 모든 데이터 삭제 (토큰, 언어 설정 등)
        dataStore.edit { preferences ->
            preferences.clear()
        }
        println("🔥 [DefaultAuthRepository] DataStore 초기화 완료")

        // 2. 모든 Repository의 인메모리 캐시 일괄 초기화
        val cacheableRepos = koin.getAll<CacheableRepository>()
        println("🔥 [DefaultAuthRepository] 찾은 CacheableRepository: ${cacheableRepos.size}개")

        cacheableRepos.forEach { repository ->
            println("🔥 [DefaultAuthRepository] clearCache() 호출: ${repository::class.simpleName}")
            repository.clearCache()
        }

        // 3. GlobalLanguageState 초기화 (로그아웃 시 언어 설정 기본값으로 리셋)
        GlobalLanguageState.initializeLanguage(AppLanguage.ENGLISH)

        println("🔥 [DefaultAuthRepository] clearToken() 완료")
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

    // 🆕 Guest Mode 관련 메서드 구현
    override suspend fun setGuestMode(isGuest: Boolean) {
        dataStore.edit { preferences ->
            preferences[guestModeKey] = isGuest
        }
        Logger.d("[Auth]", "Guest Mode 설정: $isGuest")
    }

    override suspend fun isGuestMode(): Boolean {
        return dataStore.data
            .map { preferences -> preferences[guestModeKey] ?: false }
            .firstOrNull() ?: false
    }

    override fun observeGuestMode(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[guestModeKey] ?: false
        }
    }

    override suspend fun saveGuestProfile(profile: GuestProfile) {
        val json = Json.encodeToString(profile)
        dataStore.edit { preferences ->
            preferences[guestProfileKey] = json
        }
        Logger.d("[Auth]", "Guest 프로필 로컬 저장 완료: ${profile.language}, ${profile.industry}")
    }

    override suspend fun getGuestProfile(): GuestProfile? {
        return dataStore.data
            .map { prefs ->
                prefs[guestProfileKey]?.let { json ->
                    try {
                        Json.decodeFromString<GuestProfile>(json)
                    } catch (e: Exception) {
                        Logger.e("[Auth]", "Guest 프로필 파싱 실패: ${e.message}")
                        null
                    }
                }
            }
            .firstOrNull()
    }

    override suspend fun saveGuestChecklist(checklist: GuestChecklist) {
        val json = Json.encodeToString(checklist)
        dataStore.edit { preferences ->
            preferences[guestChecklistKey] = json
        }
        Logger.d("[Auth]", "Guest 체크리스트 저장 완료: ${checklist.checkedItems.size} 항목")
    }

    override suspend fun getGuestChecklist(): GuestChecklist? {
        return dataStore.data
            .map { prefs ->
                prefs[guestChecklistKey]?.let { json ->
                    try {
                        Json.decodeFromString<GuestChecklist>(json)
                    } catch (e: Exception) {
                        Logger.e("[Auth]", "Guest 체크리스트 파싱 실패: ${e.message}")
                        null
                    }
                }
            }
            .firstOrNull()
    }

    override suspend fun clearGuestData() {
        dataStore.edit { prefs ->
            prefs.remove(guestModeKey)
            prefs.remove(guestProfileKey)
            prefs.remove(guestChecklistKey)
        }
        Logger.d("[Auth]", "Guest 데이터 삭제 완료")
    }
}
