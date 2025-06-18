package unithon.helpjob.data.repository

import unithon.helpjob.data.model.response.MemberProfileGetRes
import unithon.helpjob.data.model.response.TokenResponse

interface AuthRepository {
    @Throws(EmailNotFoundException::class, WrongPasswordException::class)
    suspend fun signIn(email: String, password: String): TokenResponse

    suspend fun signUp(email: String, password: String): TokenResponse

    @Throws(NicknameDuplicateException::class)
    suspend fun setNickname(nickname: String)

    @Throws(UnauthorizedException::class)
    suspend fun setProfile(
        language: String,
        topikLevel: String,
        visaType: String,
        industry: String
    )

    @Throws(UnauthorizedException::class)
    suspend fun getMemberProfile(): MemberProfileGetRes

    // 🆕 이메일 인증 관련 메서드
    @Throws(EmailAlreadyInUseException::class)
    suspend fun sendEmailVerification(email: String)

    @Throws(EmailVerificationFailedException::class, EmailCodeExpiredException::class)
    suspend fun verifyEmailCode(email: String, code: String)

    // 토큰 관리
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}