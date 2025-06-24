package unithon.helpjob.data.repository

import retrofit2.Response
import retrofit2.http.Body
import unithon.helpjob.data.model.request.DocumentRequest

interface DocumentRepository {
    suspend fun postCertification(
        documentRequest: DocumentRequest
    )
}