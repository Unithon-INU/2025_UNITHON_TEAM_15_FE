package unithon.helpjob.ui.setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.EmploymentCheckRepository
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val employmentCheckRepository: EmploymentCheckRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun resetProgress() {
        try {
            employmentCheckRepository.resetProgress()
            Timber.d("진행 상황 초기화 성공")
        } catch (e: Exception) {
            Timber.e("진행 상황 초기화 실패: ${e.message}")
            throw e
        }
    }

    suspend fun logout() {
        authRepository.clearToken()
    }
}