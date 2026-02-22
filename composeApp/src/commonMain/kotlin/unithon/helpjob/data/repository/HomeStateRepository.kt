package unithon.helpjob.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import unithon.helpjob.data.model.request.Steps
import unithon.helpjob.data.model.request.UpdateEmploymentCheckRequest
import unithon.helpjob.data.model.response.EmploymentCheckRes

/**
 * HomeViewModel의 서버 데이터 상태를 관리하는 Repository
 * - 여러 ViewModel에서 공유 가능한 단일 진실 공급원 (Single Source of Truth)
 * - suspend 함수만 제공, 에러 처리는 호출자(ViewModel)가 담당
 */
class HomeStateRepository(
    private val employmentCheckRepository: EmploymentCheckRepository
) : CacheableRepository {
    /**
     * 홈 화면의 서버 데이터 상태
     */
    data class HomeState(
        val nickname: String = "",
        val email: String = "",
        val steps: List<EmploymentCheckRes> = emptyList(),
        val memberCheckStep: Steps = Steps.STEP1,
        val progressPercentage: Float = 0f
    )

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    /**
     * 홈 정보 로드 (서버에서 데이터 가져오기)
     * Accept-Language 헤더는 글로벌 플러그인이 자동 처리
     * @throws Exception 네트워크 오류 또는 서버 오류 시
     */
    suspend fun loadHomeInfo() {
        println("🔥 [HomeStateRepository] loadHomeInfo() 시작")

        val response = employmentCheckRepository.getHomeInfo()

        println("🔥 [HomeStateRepository] API 응답: nickname=${response.nickname}, email=${response.email}, progress=${response.progress}")

        val latestCheckedStep = findLatestCheckedStep(response.employmentCheckRes)

        _homeState.update {
            it.copy(
                nickname = response.nickname,
                email = response.email,
                steps = response.employmentCheckRes,
                memberCheckStep = latestCheckedStep,
                progressPercentage = response.progress / 100f
            )
        }

        println("🔥 [HomeStateRepository] loadHomeInfo() 완료! 현재 상태: nickname=${_homeState.value.nickname}, email=${_homeState.value.email}")
    }

    /**
     * 문서 체크 상태 업데이트
     * @throws Exception 네트워크 오류 또는 서버 오류 시
     */
    suspend fun updateDocumentCheck(
        submissionIdx: Int,
        checkStep: Steps,
        isChecked: Boolean
    ) {
        val request = UpdateEmploymentCheckRequest(
            checkStep = checkStep.apiStep,
            submissionIdx = submissionIdx
        )

        val newProgress = employmentCheckRepository.updateChecklist(request)

        // 로컬 상태 업데이트
        _homeState.update { currentState ->
            val updatedSteps = currentState.steps.map { step ->
                if (step.checkStep == checkStep.apiStep) {
                    step.copy(
                        documentInfoRes = step.documentInfoRes.map { doc ->
                            if (doc.submissionIdx == submissionIdx) {
                                doc.copy(isChecked = isChecked)
                            } else {
                                doc
                            }
                        }
                    )
                } else {
                    step
                }
            }

            currentState.copy(
                steps = updatedSteps,
                progressPercentage = newProgress.progress / 100f,
                memberCheckStep = checkStep
            )
        }
    }

    /**
     * 가장 최근에 체크한 document가 있는 step을 찾는 함수
     */
    private fun findLatestCheckedStep(steps: List<EmploymentCheckRes>): Steps {
        val stepOrder = listOf(Steps.STEP4, Steps.STEP3, Steps.STEP2, Steps.STEP1)

        for (step in stepOrder) {
            val stepData = steps.find { it.checkStep == step.apiStep }
            if (stepData != null && stepData.documentInfoRes.any { it.isChecked }) {
                return step
            }
        }

        return Steps.STEP1
    }

    /**
     * 인메모리 캐시 초기화 (로그아웃 시 호출)
     */
    override fun clearCache() {
        println("🔥 [HomeStateRepository] clearCache() 호출됨!")
        _homeState.value = HomeState()
        println("🔥 [HomeStateRepository] clearCache() 완료! 현재 상태: nickname=${_homeState.value.nickname}, email=${_homeState.value.email}")
    }
}
