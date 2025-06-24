package unithon.helpjob.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import unithon.helpjob.data.model.request.Steps
import unithon.helpjob.data.model.request.UpdateEmploymentCheckRequest
import unithon.helpjob.data.model.response.DocumentInfoRes
import unithon.helpjob.data.model.response.EmploymentCheckRes
import unithon.helpjob.data.model.response.TipResponseItem
import unithon.helpjob.data.repository.EmploymentCheckRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val employmentCheckRepository: EmploymentCheckRepository
) : ViewModel() {

    enum class Category {
        DOCUMENTS, PRECAUTIONS
    }

    data class HomeUiState(
        val nickname: String = "",
        val email: String = "",
        val memberCheckStep: Steps = Steps.STEP1,
        val steps : List<EmploymentCheckRes> = emptyList(),
        val tips : List<TipResponseItem> = emptyList(),
        val selectedCategory: Category = Category.DOCUMENTS,
        val selectedStep: EmploymentCheckRes? = null,
        val progressPercentage: Float = 0f,
        val showStepWarningDialog: Boolean = false,
        val pendingCheckAction: (() -> Unit)? = null,
        val isLoading: Boolean = false,
        val isUpdating: Boolean = false,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /**
     * 🆕 가장 최근에 체크한 document가 있는 step을 찾는 함수
     */
    private fun findLatestCheckedStep(steps: List<EmploymentCheckRes>): Steps {
        // Step 순서를 역순으로 정렬 (STEP4 -> STEP3 -> STEP2 -> STEP1)
        val stepOrder = listOf(Steps.STEP4, Steps.STEP3, Steps.STEP2, Steps.STEP1)

        for (step in stepOrder) {
            val stepData = steps.find { it.checkStep == step.apiStep }
            if (stepData != null && stepData.documentInfoRes.any { it.isChecked }) {
                Timber.d("가장 최근 체크된 step 발견: ${step.uiStep}")
                return step
            }
        }

        // 체크된 document가 없으면 STEP1 반환
        Timber.d("체크된 document가 없으므로 STEP1 반환")
        return Steps.STEP1
    }

    fun selectStep(step: EmploymentCheckRes){
        if (_uiState.value.selectedStep?.checkStep == step.checkStep) {
            Timber.d("이미 같은 step이 선택되어 있습니다: ${step.checkStep}")
            return
        }
        _uiState.update {
            it.copy(
                selectedStep = step
            )
        }
        getTips(Steps.valueOf(step.checkStep))
    }

    fun clearSelectedStep(){
        _uiState.update {
            it.copy(
                selectedStep = null
            )
        }
    }

    fun selectCategory(category: Category){
        _uiState.update {
            it.copy(
                selectedCategory = category
            )
        }
    }

    /**
     * 문서 체크 상태 변경 시도
     */
    fun onDocumentCheckChanged(document: DocumentInfoRes, stepCheckStep: String, isChecked: Boolean) {
        if (uiState.value.isUpdating) {
            Timber.d("이미 업데이트 중입니다. 요청 무시.")
            return
        }
        val targetStep = Steps.valueOf(stepCheckStep)

        // 체크를 하려고 하고, 다음 단계이면서, (현재 단계가 완료되지 않았거나 단계를 건너뛰는 경우)에만 경고 표시
        if (isChecked && isNextStep(targetStep) && (!isCurrentStepCompleted() || !isImmediateNextStep(targetStep))) {
            showStepWarningDialog {
                updateDocumentCheck(document, stepCheckStep, isChecked)
            }
        } else {
            // 체크 해제이거나 현재/이전 단계이거나 순차적 진행인 경우 바로 처리
            updateDocumentCheck(document, stepCheckStep, isChecked)
        }
    }

    /**
     * 현재 단계보다 다음 단계인지 확인
     */
    private fun isNextStep(targetStep: Steps): Boolean {
        val currentStep = _uiState.value.memberCheckStep
        return when (currentStep) {
            Steps.STEP1 -> targetStep == Steps.STEP2 || targetStep == Steps.STEP3 || targetStep == Steps.STEP4
            Steps.STEP2 -> targetStep == Steps.STEP3 || targetStep == Steps.STEP4
            Steps.STEP3 -> targetStep == Steps.STEP4
            Steps.STEP4 -> false
        }
    }

    /**
     * 🆕 바로 다음 단계인지 확인 (순차적 진행)
     */
    private fun isImmediateNextStep(targetStep: Steps): Boolean {
        val currentStep = _uiState.value.memberCheckStep
        return when (currentStep) {
            Steps.STEP1 -> targetStep == Steps.STEP2
            Steps.STEP2 -> targetStep == Steps.STEP3
            Steps.STEP3 -> targetStep == Steps.STEP4
            Steps.STEP4 -> false
        }
    }

    /**
     * 🆕 현재 step의 모든 문서가 체크되었는지 확인
     */
    private fun isCurrentStepCompleted(): Boolean {
        val currentStep = _uiState.value.memberCheckStep
        val currentStepData = _uiState.value.steps.find { it.checkStep == currentStep.apiStep }
        return currentStepData?.documentInfoRes?.all { it.isChecked } ?: false
    }

    /**
     * 경고 다이얼로그 표시
     */
    private fun showStepWarningDialog(pendingAction: () -> Unit) {
        _uiState.update {
            it.copy(
                showStepWarningDialog = true,
                pendingCheckAction = pendingAction
            )
        }
    }

    /**
     * 경고 다이얼로그 닫기
     */
    fun dismissStepWarningDialog() {
        _uiState.update {
            it.copy(
                showStepWarningDialog = false,
                pendingCheckAction = null
            )
        }
    }

    /**
     * 경고 다이얼로그에서 계속 진행 선택
     */
    fun continueWithCheck() {
        val pendingAction = _uiState.value.pendingCheckAction
        dismissStepWarningDialog()
        pendingAction?.invoke()
    }

    /**
     * 서버와 연동된 문서 체크 상태 업데이트
     */
    private fun updateDocumentCheck(document: DocumentInfoRes, stepCheckStep: String, isChecked: Boolean) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isUpdating = true,
                        errorMessage = null
                    )
                }

                // 서버에 업데이트 요청
                val request = UpdateEmploymentCheckRequest(
                    checkStep = stepCheckStep,
                    submissionIdx = document.submissionIdx
                )

                val newProgress = employmentCheckRepository.updateChecklist(request)
                Timber.d("체크리스트 업데이트 성공 - 새로운 진행률: $newProgress")

                // 서버 요청 성공 시 로컬 상태 업데이트
                _uiState.update { currentState ->
                    val updatedSteps = currentState.steps.map { step ->
                        if (step.checkStep == stepCheckStep) {
                            step.copy(
                                documentInfoRes = step.documentInfoRes.map { doc ->
                                    if (doc.submissionIdx == document.submissionIdx) {
                                        doc.copy(
                                            isChecked = isChecked,
                                        )
                                    } else {
                                        doc
                                    }
                                }
                            )
                        } else {
                            step
                        }
                    }

                    // 🔥 핵심 변경: 가장 최근에 체크한 document가 있는 step을 찾아서 설정
                    val newMemberCheckStep = Steps.valueOf(stepCheckStep)

                    Timber.d("업데이트된 memberCheckStep: ${newMemberCheckStep.uiStep}")

                    currentState.copy(
                        steps = updatedSteps,
                        progressPercentage = newProgress.progress / 100f,
                        memberCheckStep = newMemberCheckStep,
                        isUpdating = false
                    )
                }

            } catch (e: Exception) {
                Timber.e(e, "체크리스트 업데이트 실패")
                _uiState.update {
                    it.copy(
                        isUpdating = false,
                        errorMessage = "문서 상태 업데이트에 실패했습니다. 다시 시도해주세요."
                    )
                }
            }
        }
    }

    /**
     * 에러 메시지 클리어
     */
    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    init {
        getStepInfo()
    }

    fun getStepInfo(language: String? = null){
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                // 🔄 이 부분을 수정
                val response = if (language != null) {
                    employmentCheckRepository.getHomeInfo(language)
                } else {
                    employmentCheckRepository.getHomeInfo()
                }

                Timber.d(response.toString())

                // 🔥 핵심 변경: 서버에서 받은 데이터를 기반으로 가장 최근 체크한 step 계산
                val latestCheckedStep = findLatestCheckedStep(response.employmentCheckRes)

                _uiState.update {
                    it.copy(
                        steps = response.employmentCheckRes,
                        nickname = response.nickname,
                        email = response.email,
                        progressPercentage = response.progress / 100f,
                        memberCheckStep = latestCheckedStep, // 계산된 step 사용
                        isLoading = false
                    )
                }
            } catch (e: Exception){
                Timber.e(e, "홈 정보 조회 실패")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "정보를 불러오는데 실패했습니다. 다시 시도해주세요."
                    )
                }
            }
        }
    }

    fun getTips(step: Steps){
        viewModelScope.launch {
            try {
                val response = employmentCheckRepository.getTips(step)
                Timber.d(response.toString())
                _uiState.update {
                    it.copy(
                        tips = response
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "팁 정보 조회 실패")
                // 팁 조회 실패는 치명적이지 않으므로 에러 메시지 표시하지 않음
            }
        }
    }

    /**
     * 데이터 새로고침
     */
    fun refresh() {
        getStepInfo()
        // 선택된 단계가 있으면 팁도 다시 로드
        uiState.value.selectedStep?.let { selectedStep ->
            getTips(Steps.valueOf(selectedStep.checkStep))
        }
    }

    /**
     * 특정 언어로 데이터 새로고침
     */
    fun refresh(language: String) {
        getStepInfo(language)
        // 선택된 단계가 있으면 팁도 다시 로드
        uiState.value.selectedStep?.let { selectedStep ->
            getTips(Steps.valueOf(selectedStep.checkStep))
        }
    }
}