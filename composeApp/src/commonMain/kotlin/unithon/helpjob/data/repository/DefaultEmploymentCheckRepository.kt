package unithon.helpjob.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
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
     * - Structured Concurrency 준수: 호출자가 취소를 제어
     * - supervisorScope로 개별 실패 시에도 다른 항목은 계속 동기화
     */
    override suspend fun syncGuestDataToServer() {
        withContext(Dispatchers.Default) {
            val guestChecklist = authRepository.getGuestChecklist()

            if (guestChecklist == null || guestChecklist.checkedItems.isEmpty()) {
                Logger.d("[Sync]", "Guest 체크리스트가 비어있어 동기화 스킵")
                return@withContext
            }

            Logger.d("[Sync]", "Guest 데이터 동기화 시작: ${guestChecklist.checkedItems.size} 단계")

            try {
                // supervisorScope: 하나가 실패해도 나머지는 계속 진행
                supervisorScope {
                    guestChecklist.checkedItems.forEach { (step, indices) ->
                        indices.forEach { idx ->
                            launch {
                                try {
                                    apiService.updateChecklist(
                                        UpdateEmploymentCheckRequest(step, idx)
                                    )
                                    Logger.d("[Sync]", "성공: $step-$idx")
                                } catch (e: Exception) {
                                    // 개별 실패는 로그만 기록 (사용자에게 알리지 않음)
                                    Logger.e("[Sync]", "실패: $step-$idx - ${e.message}")
                                }
                            }
                        }
                    }
                }

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
