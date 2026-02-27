package unithon.helpjob.data.repository

import unithon.helpjob.data.model.request.DocumentRequest
import unithon.helpjob.data.model.response.UniversityInfo
import unithon.helpjob.data.model.response.WorkingTimeLimitResponse
import unithon.helpjob.data.network.HelpJobApiService
// TODO[LEGACY]: import unithon.helpjob.data.model.response.UniversityResponse

class DefaultDocumentRepository(
    private val apiService: HelpJobApiService,
    private val languageRepository: LanguageRepository
) : DocumentRepository {
    override suspend fun postCertification(documentRequest: DocumentRequest) {
        // Accept-Language 헤더는 글로벌 플러그인이 자동 처리
        apiService.postCertification(documentRequest)
    }

    // TODO[LEGACY]: 대학 검색 API 재통합 시 해제
    // override suspend fun searchUniversity(university: String): List<UniversityResponse> {
    //     return apiService.searchUniversity(university)
    // }

    override suspend fun getAccreditedUniversities(): List<UniversityInfo> {
        return apiService.getAccreditedUniversities().university
    }

    override suspend fun getWorkingTimeLimit(
        isAccredited: Boolean,
        year: String
    ): WorkingTimeLimitResponse {
        return apiService.getWorkingTimeLimit(isAccredited, year)
    }
}
