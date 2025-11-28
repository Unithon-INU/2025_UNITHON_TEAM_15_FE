package unithon.helpjob.data.repository

import unithon.helpjob.data.model.request.Steps
import unithon.helpjob.data.model.request.UpdateEmploymentCheckRequest
import unithon.helpjob.data.model.response.HomeInfoResponse
import unithon.helpjob.data.model.response.TipResponseItem
import unithon.helpjob.data.model.response.UpdateEmploymentCheckResponse
import unithon.helpjob.data.network.HelpJobApiService

class DefaultEmploymentCheckRepository(
    private val apiService: HelpJobApiService,
    private val languageRepository: LanguageRepository,
): EmploymentCheckRepository {
    override suspend fun updateChecklist(request: UpdateEmploymentCheckRequest): UpdateEmploymentCheckResponse {
        return apiService.updateChecklist(request)
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    override suspend fun getHomeInfo(): HomeInfoResponse {
        return getHomeInfo(languageRepository.getCurrentLanguage().code)
    }

    override suspend fun getHomeInfo(language: String): HomeInfoResponse {
        return apiService.getHomeInfo(language)
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    override suspend fun getTips(language: String, checkStep: Steps): List<TipResponseItem> {
        return apiService.getTips(language = language, checkStep.apiStep)
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    override suspend fun getTips(checkStep: Steps): List<TipResponseItem> {
        return apiService.getTips(
            language = languageRepository.getCurrentLanguage().code,
            checkStep.apiStep
        )
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    override suspend fun resetProgress() {
        apiService.resetProgress()
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }
}
