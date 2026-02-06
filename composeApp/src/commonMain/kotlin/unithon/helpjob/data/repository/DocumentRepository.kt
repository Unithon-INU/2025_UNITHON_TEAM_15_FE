package unithon.helpjob.data.repository

import unithon.helpjob.data.model.request.DocumentRequest
import unithon.helpjob.data.model.response.UniversityResponse
import unithon.helpjob.data.model.response.WorkingTimeLimitResponse

interface DocumentRepository {
    suspend fun postCertification(documentRequest: DocumentRequest)
    suspend fun searchUniversity(universityName: String): List<UniversityResponse>
    suspend fun getWorkingTimeLimit(university: String, major: String, year: String): WorkingTimeLimitResponse
}
