package unithon.helpjob.data.network

import retrofit2.Response
import retrofit2.http.POST

interface HelpJobApiService {

    @POST(ApiConstants.SIGN_IN)
    suspend fun signIn(): Response<Unit>

    @POST(ApiConstants.SIGN_UP)
    suspend fun signUp(): Response<Unit>

}