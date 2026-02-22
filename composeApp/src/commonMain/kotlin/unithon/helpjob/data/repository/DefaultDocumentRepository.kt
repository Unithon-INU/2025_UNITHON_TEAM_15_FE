package unithon.helpjob.data.repository

import unithon.helpjob.data.model.request.DocumentRequest
import unithon.helpjob.data.model.response.UniversityResponse
import unithon.helpjob.data.model.response.WorkingTimeLimitResponse
import unithon.helpjob.data.network.HelpJobApiService

class DefaultDocumentRepository(
    private val apiService: HelpJobApiService,
    private val languageRepository: LanguageRepository
) : DocumentRepository {
    override suspend fun postCertification(documentRequest: DocumentRequest) {
        // Accept-Language 헤더는 글로벌 플러그인이 자동 처리
        apiService.postCertification(documentRequest)
    }

    override suspend fun searchUniversity(university: String): List<UniversityResponse> {
        return apiService.searchUniversity(university)
    }

    override suspend fun getWorkingTimeLimit(
        university: String,
        major: String,
        year: String
    ): WorkingTimeLimitResponse {
        return apiService.getWorkingTimeLimit(university, major, year)
    }
}
