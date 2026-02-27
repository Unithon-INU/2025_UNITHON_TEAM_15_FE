package unithon.helpjob.data.repository

import unithon.helpjob.data.model.request.DocumentRequest
import unithon.helpjob.data.model.response.UniversityInfo
import unithon.helpjob.data.model.response.WorkingTimeLimitResponse
// TODO[LEGACY]: import unithon.helpjob.data.model.response.UniversityResponse

interface DocumentRepository {
    suspend fun postCertification(documentRequest: DocumentRequest)
    // TODO[LEGACY]: 대학 검색 API 재통합 시 해제
    // suspend fun searchUniversity(university: String): List<UniversityResponse>
    suspend fun getAccreditedUniversities(): List<UniversityInfo>
    suspend fun getWorkingTimeLimit(isAccredited: Boolean, year: String): WorkingTimeLimitResponse
}
