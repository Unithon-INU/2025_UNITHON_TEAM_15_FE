package unithon.helpjob.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import unithon.helpjob.data.model.request.EmailSendReq
import unithon.helpjob.data.model.request.EmailVerifyCodeReq
import unithon.helpjob.data.model.request.MemberNicknameReq
import unithon.helpjob.data.model.request.MemberProfileSetReq
import unithon.helpjob.data.model.request.MemberSignInReq
import unithon.helpjob.data.model.request.MemberSignUpReq
import unithon.helpjob.data.model.request.UpdateEmploymentCheckRequest
import unithon.helpjob.data.model.response.HomeInfoResponse
import unithon.helpjob.data.model.response.MemberProfileGetRes
import unithon.helpjob.data.model.response.TipResponseItem
import unithon.helpjob.data.model.response.TokenResponse
import unithon.helpjob.data.model.response.UpdateEmploymentCheckResponse

interface HelpJobApiService {
    // 회원 관련 API
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
        @Body request: MemberProfileSetReq
    ): Response<TokenResponse>

    @GET(ApiConstants.GET_PROFILE)
    suspend fun getMemberProfile(): Response<MemberProfileGetRes>

    // 🆕 이메일 인증 관련 API
    @POST(ApiConstants.EMAIL_SEND)
    suspend fun sendEmailVerification(
        @Body request: EmailSendReq
    ): Response<Unit>

    @POST(ApiConstants.EMAIL_VERIFY)
    suspend fun verifyEmailCode(
        @Body request: EmailVerifyCodeReq
    ): Response<Unit>

    // 시간제 취업 확인 관련 API
    @PATCH(ApiConstants.UPDATE_CHECKLIST)
    suspend fun updateChecklist(
        @Body request: UpdateEmploymentCheckRequest
    ): Response<UpdateEmploymentCheckResponse>

    @GET(ApiConstants.GET_HOME_INFO)
    suspend fun getHomeInfo() : Response<HomeInfoResponse>

    @GET(ApiConstants.GET_TIPS)
    suspend fun getTips(
        @Query("checkStep") checkStep: String
    ) : Response<List<TipResponseItem>>

    @PUT(ApiConstants.RESET_PROGRESS)
    suspend fun resetProgress(): Response<Unit>
}