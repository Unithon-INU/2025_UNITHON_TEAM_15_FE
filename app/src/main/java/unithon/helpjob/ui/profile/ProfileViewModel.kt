package unithon.helpjob.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import unithon.helpjob.data.repository.AuthRepository
import javax.inject.Inject

data class ProfileUiState(
    val visaType: String? = null,
    val topikLevel: String? = null,
    val industry: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: Int? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val memberProfile = authRepository.getMemberProfile()

                _uiState.value = _uiState.value.copy(
                    visaType = memberProfile.visaType,
                    topikLevel = memberProfile.topikLevel,
                    industry = memberProfile.industry,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                Timber.e(e, "프로필 로딩 실패")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }
}