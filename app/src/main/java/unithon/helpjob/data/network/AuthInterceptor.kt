package unithon.helpjob.data.network

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import unithon.helpjob.data.repository.dataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {

    private val tokenKey = stringPreferencesKey("auth_token")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // 토큰이 필요없는 API들 (로그인, 회원가입 등)
        val noAuthEndpoints = listOf(
            "/api/member/sign-in",
            "/api/member/sign-up",
            "/api/email/send",
            "/api/email/verify"
        )

        if (noAuthEndpoints.any { request.url.encodedPath.contains(it) }) {
            return chain.proceed(request)
        }

        // DataStore에서 직접 토큰 가져오기
        val token = runBlocking {
            context.dataStore.data
                .map { preferences -> preferences[tokenKey] }
                .firstOrNull()
        }

        // 토큰이 있으면 헤더에 추가
        val authenticatedRequest = if (token != null) {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(authenticatedRequest)
    }
}