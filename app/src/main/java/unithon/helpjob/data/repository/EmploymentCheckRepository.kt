package unithon.helpjob.data.repository

import unithon.helpjob.data.model.request.Steps
import unithon.helpjob.data.model.request.UpdateChecklistRequest
import unithon.helpjob.data.model.response.HomeInfoResponse
import unithon.helpjob.data.model.response.TipResponseItem

interface EmploymentCheckRepository {
    suspend fun updateChecklist(
        request: UpdateChecklistRequest
    ): Unit

    suspend fun getHomeInfo() : HomeInfoResponse

    suspend fun getTips(
        checkStep: Steps
    ) : List<TipResponseItem>
}