package unithon.helpjob.data.repository

import kotlinx.coroutines.flow.Flow
import unithon.helpjob.data.model.response.MemberProfileGetRes
import unithon.helpjob.data.model.response.TokenResponse
import kotlin.coroutines.cancellation.CancellationException

interface AuthRepository {
    @Throws(EmailNotFoundException::class, WrongPasswordException::class, CancellationException::class)
    suspend fun signIn(email: String, password: String): TokenResponse

    suspend fun signUp(email: String, password: String): TokenResponse

    @Throws(NicknameDuplicateException::class, CancellationException::class)
    suspend fun setNickname(nickname: String)

    @Throws(UnauthorizedException::class, CancellationException::class)
    suspend fun setProfile(
        language: String,
        topikLevel: String,
        visaType: String,
        industry: String
    )

    @Throws(UnauthorizedException::class, CancellationException::class)
    suspend fun getMemberProfile(): MemberProfileGetRes

    // 🆕 이메일 인증 관련 메서드
    @Throws(EmailAlreadyInUseException::class, CancellationException::class)
    suspend fun sendEmailVerification(email: String)

    @Throws(EmailVerificationFailedException::class, EmailCodeExpiredException::class, CancellationException::class)
    suspend fun verifyEmailCode(email: String, code: String)

    // 토큰 관리
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()

    // 🆕 온보딩 완료 여부 체크
    suspend fun isOnboardingCompleted(): Boolean

    // 🆕 Guest Mode 관련 메서드
    suspend fun setGuestMode(isGuest: Boolean)
    suspend fun isGuestMode(): Boolean
    fun observeGuestMode(): Flow<Boolean>  // 🆕 실시간 구독

    suspend fun saveGuestProfile(profile: unithon.helpjob.data.model.GuestProfile)
    suspend fun getGuestProfile(): unithon.helpjob.data.model.GuestProfile?

    suspend fun saveGuestChecklist(checklist: unithon.helpjob.data.model.GuestChecklist)
    suspend fun getGuestChecklist(): unithon.helpjob.data.model.GuestChecklist?

    suspend fun clearGuestData()
}
