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
        apiService.postCertification(
            language = languageRepository.getCurrentLanguage().code,
            documentRequest = documentRequest
        )
    }

    override suspend fun searchUniversity(universityName: String): List<UniversityResponse> {
        return apiService.searchUniversity(universityName)
    }

    override suspend fun getWorkingTimeLimit(
        university: String,
        major: String,
        year: String
    ): WorkingTimeLimitResponse {
        return apiService.getWorkingTimeLimit(university, major, year)
    }
}
