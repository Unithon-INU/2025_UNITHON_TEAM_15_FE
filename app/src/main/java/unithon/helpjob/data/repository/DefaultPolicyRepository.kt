package unithon.helpjob.data.repository

import unithon.helpjob.data.network.HelpJobApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPolicyRepository @Inject constructor(
    private val apiService: HelpJobApiService
) : PolicyRepository {

    override suspend fun getPrivacyPolicy(): String {
        val response = apiService.getPrivacyPolicy()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("응답이 비어있습니다")
        }
        throw Exception(response.errorBody()?.string() ?: "개인정보처리방침 조회 실패")
    }

    override suspend fun getTermsOfService(): String {
        val response = apiService.getTermsOfService()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("응답이 비어있습니다")
        }
        throw Exception(response.errorBody()?.string() ?: "이용약관 조회 실패")
    }
}