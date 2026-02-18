package unithon.helpjob.ui.main

import androidx.lifecycle.viewModelScope
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.error_update_checklist
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import unithon.helpjob.util.Logger
import unithon.helpjob.data.model.request.Steps
import unithon.helpjob.data.model.response.DocumentInfoRes
import unithon.helpjob.data.model.response.EmploymentCheckRes
import unithon.helpjob.data.model.response.TipResponseItem
import unithon.helpjob.data.repository.EmploymentCheckRepository
import unithon.helpjob.data.repository.HomeStateRepository
import unithon.helpjob.data.repository.LanguageRepository
import unithon.helpjob.ui.base.BaseViewModel
import unithon.helpjob.util.Analytics

class HomeViewModel(
    private val employmentCheckRepository: EmploymentCheckRepository,
    private val homeStateRepository: HomeStateRepository,
    private val languageRepository: LanguageRepository
) : BaseViewModel() {

    enum class Category {
        DOCUMENTS, PRECAUTIONS
    }

    /**
     * UI 전용 상태 (선택된 카테고리, 팁, 다이얼로그 등)
     */
    data class HomeUiState(
        val selectedCategory: Category = Category.DOCUMENTS,
        val selectedStep: EmploymentCheckRes? = null,
        val tips: List<TipResponseItem> = emptyList(),
        val showStepWarningDialog: Boolean = false,
        val isRefreshing: Boolean = false,
        val pendingCheckAction: (() -> Unit)? = null
    )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Repository 상태 노출
    val homeState = homeStateRepository.homeState

    private val _snackbarMessage = MutableSharedFlow<StringResource>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {
        refresh()
        observeLanguageChanges()
    }

    /**
     * 언어 변경 감지하여 자동 갱신
     */
    private fun observeLanguageChanges() {
        viewModelScope.launch(crashPreventionHandler) {
            languageRepository.currentLanguage
                .drop(1)  // 첫 값 무시 (중복 방지)
                .collect { language ->
                    Logger.d("🌐 언어 변경 감지: ${language.code}")
                    // Accept-Language 헤더는 글로벌 플러그인이 자동 처리
                    homeStateRepository.loadHomeInfo()
                    _uiState.value.selectedStep?.let { selectedStep ->
                        getTips(Steps.valueOf(selectedStep.checkStep))
                    }
                }
        }
    }

    /**
     * 가장 최근에 체크한 document가 있는 step을 찾는 함수
     * (Repository에서 계산하므로 이제 불필요하지만, UI 로직에 필요하면 유지)
     */
    private fun areAllPreviousStepsCompleted(targetStep: Steps): Boolean {
        val stepOrder = listOf(Steps.STEP1, Steps.STEP2, Steps.STEP3, Steps.STEP4)
        val targetIndex = stepOrder.indexOf(targetStep)

        if (targetIndex <= 0) return true

        for (i in 0 until targetIndex) {
            val stepToCheck = stepOrder[i]
            val stepData = homeState.value.steps.find { it.checkStep == stepToCheck.apiStep }

            val isStepCompleted = stepData?.documentInfoRes?.all { it.isChecked } ?: false
            if (!isStepCompleted) {
                Logger.d("${stepToCheck.uiStep}이 완료되지 않았습니다.")
                return false
            }
        }

        Logger.d("${targetStep.uiStep} 이전의 모든 단계가 완료되었습니다.")
        return true
    }

    fun selectStep(step: EmploymentCheckRes) {
        Logger.d("🔍 selectStep 호출: ${step.checkStep}")
        if (_uiState.value.selectedStep?.checkStep == step.checkStep) {
            Logger.d("이미 같은 step이 선택되어 있습니다: ${step.checkStep}")
            return
        }
        _uiState.update {
            it.copy(selectedStep = step)
        }
        Logger.d("✅ selectedStep 업데이트 완료: ${_uiState.value.selectedStep?.checkStep}")
        getTips(Steps.valueOf(step.checkStep))
    }

    fun clearSelectedStep() {
        _uiState.update {
            it.copy(selectedStep = null)
        }
    }

    fun selectCategory(category: Category) {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
    }

    /**
     * 문서 체크 상태 변경 시도
     */
    fun onDocumentCheckChanged(document: DocumentInfoRes, stepCheckStep: String, isChecked: Boolean) {
        val targetStep = Steps.valueOf(stepCheckStep)

        // 체크를 하려고 하고, 이전 단계들이 완료되지 않은 경우에만 경고 표시
        if (isChecked && !areAllPreviousStepsCompleted(targetStep)) {
            showStepWarningDialog {
                updateDocumentCheck(document, stepCheckStep, isChecked)
            }
        } else {
            updateDocumentCheck(document, stepCheckStep, isChecked)
        }
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
        viewModelScope.launch(crashPreventionHandler) {
            try {
                homeStateRepository.updateDocumentCheck(
                    submissionIdx = document.submissionIdx,
                    checkStep = Steps.valueOf(stepCheckStep),
                    isChecked = isChecked
                )
                Logger.d("체크리스트 업데이트 성공")
                Analytics.logEvent(
                    "checklist_updated",
                    mapOf("step" to stepCheckStep)
                )
            } catch (e: Exception) {
                Logger.e(e, "체크리스트 업데이트 실패")
                _snackbarMessage.emit(Res.string.error_update_checklist)
            }
        }
    }

    private fun getTips(step: Steps) {
        viewModelScope.launch(crashPreventionHandler) {
            try {
                val response = employmentCheckRepository.getTips(step)
                Logger.d(response.toString())
                _uiState.update {
                    it.copy(tips = response)
                }
            } catch (e: Exception) {
                Logger.e(e, "팁 정보 조회 실패")
            }
        }
    }

    /**
     * 데이터 새로고침
     */
    fun refresh() {
        viewModelScope.launch(crashPreventionHandler) {
            _uiState.update { it.copy(isRefreshing = true) }
            try {
                homeStateRepository.loadHomeInfo()
                // 선택된 단계가 있으면 팁도 다시 로드
                _uiState.value.selectedStep?.let { selectedStep ->
                    getTips(Steps.valueOf(selectedStep.checkStep))
                }
            } finally {
                _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }
}
