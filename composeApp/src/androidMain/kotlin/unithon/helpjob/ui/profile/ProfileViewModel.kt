package unithon.helpjob.ui.profile

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import unithon.helpjob.R
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.ui.base.BaseViewModel
import javax.inject.Inject

data class ProfileUiState(
    val visaType: String? = null,
    val topikLevel: String? = null,
    val industry: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Int>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch(crashPreventionHandler) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val memberProfile = authRepository.getMemberProfile()

                _uiState.value = _uiState.value.copy(
                    visaType = memberProfile.visaType,
                    topikLevel = memberProfile.topikLevel,
                    industry = memberProfile.industry,
                    isLoading = false
                )
            } catch (e: Exception) {
                Timber.e(e, "프로필 로딩 실패")
                _snackbarMessage.emit(R.string.error_profile_load_failed)  // 이 줄 추가
                _uiState.value = _uiState.value.copy(
                    isLoading = false
                )
            }
        }
    }
}