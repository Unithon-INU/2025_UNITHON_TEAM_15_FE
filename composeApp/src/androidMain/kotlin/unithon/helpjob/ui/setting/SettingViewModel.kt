package unithon.helpjob.ui.setting

import androidx.lifecycle.viewModelScope
import dev.icerock.moko.resources.StringResource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import unithon.helpjob.resources.MR
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
                Timber.d("진행 상황 초기화 성공")
            } catch (e: Exception) {
                // Critical Error - 사용자에게 알림
                _snackbarMessage.emit(MR.strings.reset_progress_error)
                Timber.e(e, "진행 상황 초기화 실패")
            }
        }
    }

    fun logout() {
        viewModelScope.launch(crashPreventionHandler) {
            authRepository.clearToken()
            // 로그아웃은 실패할 가능성이 거의 없으므로 에러 처리 불필요
        }
    }
}