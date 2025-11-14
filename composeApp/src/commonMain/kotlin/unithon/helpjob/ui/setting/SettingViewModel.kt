package unithon.helpjob.ui.setting

import androidx.lifecycle.viewModelScope
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.reset_progress_error
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.EmploymentCheckRepository
import unithon.helpjob.ui.base.BaseViewModel

class SettingViewModel(
    private val employmentCheckRepository: EmploymentCheckRepository,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _snackbarMessage = MutableSharedFlow<StringResource>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    fun resetProgress() {
        viewModelScope.launch(crashPreventionHandler) {
            try {
                employmentCheckRepository.resetProgress()
                println("[SettingViewModel] 진행 상황 초기화 성공")
            } catch (e: Exception) {
                _snackbarMessage.emit(Res.string.reset_progress_error)
                println("[SettingViewModel] 진행 상황 초기화 실패: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch(crashPreventionHandler) {
            authRepository.clearToken()
        }
    }
}
