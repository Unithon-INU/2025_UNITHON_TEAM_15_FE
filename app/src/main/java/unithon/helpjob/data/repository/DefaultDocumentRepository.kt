package unithon.helpjob.data.repository

import unithon.helpjob.data.model.request.DocumentRequest
import unithon.helpjob.data.model.request.MemberNicknameReq
import unithon.helpjob.data.network.HelpJobApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultDocumentRepository @Inject constructor(
    private val apiService: HelpJobApiService
) : DocumentRepository{
    override suspend fun postCertification(documentRequest: DocumentRequest) {
        val response = apiService.postCertification(documentRequest = documentRequest)

        if (response.isSuccessful) {
            return
        }
        throw Exception(response.errorBody()?.string() ?: "서류 작성 실패")
    }
}