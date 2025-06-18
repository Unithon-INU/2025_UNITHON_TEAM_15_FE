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
import unithon.helpjob.data.model.response.EmploymentCheckRes
import unithon.helpjob.data.model.response.TipResponseItem
import unithon.helpjob.data.repository.EmploymentCheckRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val employmentCheckRepository: EmploymentCheckRepository
) : ViewModel() {

    data class HomeUiState(
        val nickname: String = "",
        val memberCheckStep: Steps = Steps.STEP1,
        val steps : List<EmploymentCheckRes> = emptyList(),
        val tips : List<TipResponseItem> = emptyList(),
        val selectedCategory: String = "제출 서류",
        val selectedStep: EmploymentCheckRes? = null,
        val progressPercentage: Float = 0f,
    )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun selectStep(step: EmploymentCheckRes){
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

    fun selectCategory(category: String){
        _uiState.update {
            it.copy(
                selectedCategory = category
            )
        }
    }

    init {
        getStepInfo()
    }

    fun getStepInfo(){
        viewModelScope.launch {
            val response = employmentCheckRepository.getHomeInfo()
            Timber.d(response.toString())
            _uiState.update {
                it.copy(
                    steps = response.employmentCheckRes,
                    nickname = response.nickname,
                    progressPercentage = response.progress / 100f,
                    memberCheckStep = Steps.valueOf(response.memberCheckStep)
                )
            }

        }
    }

    fun getTips(step: Steps){
        viewModelScope.launch {
            val response = employmentCheckRepository.getTips(step)
            Timber.d(response.toString())
            _uiState.update {
                it.copy(
                    tips = response
                )
            }
        }
    }
}