package unithon.helpjob.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import unithon.helpjob.data.model.request.EmailSendReq
import unithon.helpjob.data.model.request.EmailVerifyCodeReq
import unithon.helpjob.data.model.request.MemberNicknameReq
import unithon.helpjob.data.model.request.MemberProfileReq
import unithon.helpjob.data.model.request.MemberSignInReq
import unithon.helpjob.data.model.request.MemberSignUpReq
import unithon.helpjob.data.model.response.TokenResponse

interface HelpJobApiService {

    @POST(ApiConstants.SIGN_IN)
    suspend fun signIn(
        @Body request: MemberSignInReq
    ): Response<TokenResponse>

    @POST(ApiConstants.SIGN_UP)
    suspend fun signUp(
        @Body request: MemberSignUpReq
    ): Response<TokenResponse>

    @POST(ApiConstants.SET_NICKNAME)
    suspend fun setNickname(
        @Body request: MemberNicknameReq
    ): Response<Unit>

    @POST(ApiConstants.SET_PROFILE)
    suspend fun setProfile(
        @Body request: MemberProfileReq
    ): Response<TokenResponse>

    // 🆕 이메일 인증 관련 API
    @POST(ApiConstants.EMAIL_SEND)
    suspend fun sendEmailVerification(
        @Body request: EmailSendReq
    ): Response<Unit>

    @POST(ApiConstants.EMAIL_VERIFY)
    suspend fun verifyEmailCode(
        @Body request: EmailVerifyCodeReq
    ): Response<Unit>
}