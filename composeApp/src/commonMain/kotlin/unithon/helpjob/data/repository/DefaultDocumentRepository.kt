package unithon.helpjob.data.repository

import unithon.helpjob.data.model.request.DocumentRequest
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
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }
}
