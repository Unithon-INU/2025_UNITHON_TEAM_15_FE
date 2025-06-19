package unithon.helpjob.data.repository

import unithon.helpjob.data.model.request.Steps
import unithon.helpjob.data.model.request.UpdateEmploymentCheckRequest
import unithon.helpjob.data.model.response.HomeInfoResponse
import unithon.helpjob.data.model.response.TipResponseItem
import unithon.helpjob.data.model.response.UpdateEmploymentCheckResponse
import unithon.helpjob.data.network.HelpJobApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultEmploymentCheckRepository @Inject constructor(
    private val apiService: HelpJobApiService,
): EmploymentCheckRepository {
    override suspend fun updateChecklist(request: UpdateEmploymentCheckRequest): UpdateEmploymentCheckResponse {
        val response = apiService.updateChecklist(request)

        if (response.isSuccessful){
            response.body()?.let { progress ->
                return progress
            }
        }
        throw Exception(response.errorBody()?.string() ?: "체크리스트 업데이트 실패")
    }

    override suspend fun getHomeInfo(): HomeInfoResponse {
        val response = apiService.getHomeInfo()

        if (response.isSuccessful){
            response.body()?.let { homeInfoResponse ->
                return homeInfoResponse
            }
        }
        throw Exception(response.errorBody()?.string() ?: "홈 정보 조회 실패")
    }

    override suspend fun getTips(checkStep: Steps) : List<TipResponseItem> {
        val response = apiService.getTips(checkStep.apiStep)

        if (response.isSuccessful){
            response.body()?.let { tipResponse ->
                return tipResponse
            }
        }
        throw Exception(response.errorBody()?.string() ?: "팁 정보 조회 실패")
    }

}