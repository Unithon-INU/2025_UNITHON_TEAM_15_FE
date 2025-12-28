package unithon.helpjob.data.repository

import unithon.helpjob.data.model.request.Steps
import unithon.helpjob.data.model.request.UpdateEmploymentCheckRequest
import unithon.helpjob.data.model.response.HomeInfoResponse
import unithon.helpjob.data.model.response.TipResponseItem
import unithon.helpjob.data.model.response.UpdateEmploymentCheckResponse

interface EmploymentCheckRepository {
    suspend fun updateChecklist(
        request: UpdateEmploymentCheckRequest
    ): UpdateEmploymentCheckResponse

    suspend fun getHomeInfo() : HomeInfoResponse

    suspend fun getHomeInfo(language: String) : HomeInfoResponse

    suspend fun getTips(
        language: String,
        checkStep: Steps
    ) : List<TipResponseItem>

    suspend fun getTips(
        checkStep: Steps
    ) : List<TipResponseItem>

    suspend fun resetProgress()

    /**
     * 🆕 Guest → Member 전환 시 Guest 데이터를 서버로 동기화
     * NonCancellable + supervisorScope로 안전하게 백그라운드 실행
     */
    suspend fun syncGuestDataToServer()
}
