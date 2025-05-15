package unithon.helpjob.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import unithon.helpjob.data.model.request.*
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
        @Header("Authorization") token: String,
        @Body request: MemberNicknameReq
    ): Response<Unit>

    @POST(ApiConstants.SET_PROFILE)
    suspend fun setProfile(
        @Header("Authorization") token: String,
        @Body request: MemberProfileReq
    ): Response<TokenResponse>
}