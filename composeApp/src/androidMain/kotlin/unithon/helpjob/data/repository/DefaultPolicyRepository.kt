package unithon.helpjob.data.repository

import unithon.helpjob.data.network.HelpJobApiService

class DefaultPolicyRepository(
    private val apiService: HelpJobApiService
) : PolicyRepository {

    // json 파싱이 아니라 원시적인 html 그대로 가져오기 위해
    override suspend fun getPrivacyPolicy(): String {
        return apiService.getPrivacyPolicy()
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    override suspend fun getTermsOfService(): String {
        return apiService.getTermsOfService()
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }
}