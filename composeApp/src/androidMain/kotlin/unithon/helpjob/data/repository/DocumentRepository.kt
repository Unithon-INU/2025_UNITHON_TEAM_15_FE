package unithon.helpjob.data.repository

import unithon.helpjob.data.model.request.DocumentRequest

interface DocumentRepository {
    suspend fun postCertification(
        documentRequest: DocumentRequest
    )
}