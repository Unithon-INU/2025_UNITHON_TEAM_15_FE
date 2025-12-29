package unithon.helpjob.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import unithon.helpjob.data.model.request.Steps
import unithon.helpjob.data.model.request.UpdateEmploymentCheckRequest
import unithon.helpjob.data.model.response.HomeInfoResponse
import unithon.helpjob.data.model.response.TipResponseItem
import unithon.helpjob.data.model.response.UpdateEmploymentCheckResponse
import unithon.helpjob.data.network.HelpJobApiService
import unithon.helpjob.util.Logger

class DefaultEmploymentCheckRepository(
    private val apiService: HelpJobApiService,
    private val languageRepository: LanguageRepository,
    private val authRepository: AuthRepository,  // 🆕 Guest Mode 분기용
    private val guestDataSource: GuestMockDataSource  // 🆕 Guest Mock Data
): EmploymentCheckRepository {
    override suspend fun updateChecklist(request: UpdateEmploymentCheckRequest): UpdateEmploymentCheckResponse {
        // 🆕 Hybrid Pattern: Guest/Member 분기
        return if (authRepository.isGuestMode()) {
            guestDataSource.updateGuestChecklist(request)
        } else {
            apiService.updateChecklist(request)
        }
    }

    override suspend fun getHomeInfo(): HomeInfoResponse {
        return getHomeInfo(languageRepository.getCurrentLanguage().code)
    }

    override suspend fun getHomeInfo(language: String): HomeInfoResponse {
        // 🆕 Hybrid Pattern: Guest/Member 분기
        return if (authRepository.isGuestMode()) {
            guestDataSource.getGuestHomeInfo(language)
        } else {
            apiService.getHomeInfo(language)
        }
    }

    override suspend fun getTips(language: String, checkStep: Steps): List<TipResponseItem> {
        return apiService.getTips(language = language, checkStep.apiStep)
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    override suspend fun getTips(checkStep: Steps): List<TipResponseItem> {
        return apiService.getTips(
            language = languageRepository.getCurrentLanguage().code,
            checkStep.apiStep
        )
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    override suspend fun resetProgress() {
        apiService.resetProgress()
        // ✅ HttpResponseValidator가 자동으로 에러 처리
    }

    /**
     * 🆕 Guest → Member 백그라운드 동기화
     *
     * - Batch API로 한 번에 모든 체크리스트 전송
     * - Structured Concurrency 준수: 호출자가 취소를 제어
     */
    override suspend fun syncGuestDataToServer() {
        withContext(Dispatchers.Default) {
            val guestChecklist = authRepository.getGuestChecklist()

            if (guestChecklist == null || guestChecklist.checkedItems.isEmpty()) {
                Logger.d("[Sync]", "Guest 체크리스트가 비어있어 동기화 스킵")
                return@withContext
            }

            // Map<String, List<Int>>을 List<UpdateEmploymentCheckRequest>로 변환
            val requests = guestChecklist.checkedItems.flatMap { (step, indices) ->
                indices.map { idx ->
                    UpdateEmploymentCheckRequest(step, idx)
                }
            }

            Logger.d("[Sync]", "Guest 데이터 동기화 시작: ${requests.size}개 항목")

            try {
                // 🆕 Batch API 호출 (한 번에 모든 체크리스트 전송)
                apiService.updateChecklistBatch(requests)
                Logger.d("[Sync]", "Guest 체크리스트 동기화 성공: ${requests.size}개 항목")

                // 성공적으로 전송 완료 후 Guest 데이터 삭제
                authRepository.clearGuestData()
                Logger.d("[Sync]", "Guest 데이터 동기화 완료 및 로컬 데이터 삭제")

            } catch (e: Exception) {
                Logger.e("[Sync]", "Guest 데이터 동기화 실패: ${e.message}")
                // Guest 데이터는 유지 (다음 로그인 시 재시도 가능)
            }
        }
    }
}
