package unithon.helpjob.data.repository

import unithon.helpjob.data.model.request.DocumentRequest
import unithon.helpjob.data.network.HelpJobApiService

class DefaultDocumentRepository(
    private val apiService: HelpJobApiService,
    private val languageRepository: LanguageRepository
) : DocumentRepository {
    override suspend fun postCertification(documentRequest: DocumentRequest) {
        val response = apiService.postCertification(language = languageRepository.getCurrentLanguage().code,documentRequest = documentRequest)

        if (response.isSuccessful) {
            return
        }
        throw Exception(response.errorBody()?.string() ?: "서류 작성 실패")
    }
}