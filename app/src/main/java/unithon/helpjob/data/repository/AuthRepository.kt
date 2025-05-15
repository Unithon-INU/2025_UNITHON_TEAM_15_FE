package unithon.helpjob.data.repository

import unithon.helpjob.data.model.response.TokenResponse

interface AuthRepository {
    suspend fun signIn(email: String, password: String): TokenResponse
    suspend fun signUp(email: String, password: String): TokenResponse

    @Throws(NicknameDuplicateException::class)
    suspend fun setNickname(nickname: String)

    suspend fun setProfile(
        language: String,
        languageLevel: String,
        visaType: String,
        industry: String
    ): TokenResponse

    // 토큰 관리
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}