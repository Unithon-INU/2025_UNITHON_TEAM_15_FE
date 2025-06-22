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
import unithon.helpjob.data.repository.EmploymentCheckRepository
import javax.inject.Inject

data class ProfileUiState(
    val nickname: String? = null,
    val email: String? = null,
    val visaType: String? = null,
    val koreanLevel: String? = null,
    val preferredJob: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: Int? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val employmentCheckRepository: EmploymentCheckRepository
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
                // 1. 닉네임, 이메일 가져오기
                val homeInfo = employmentCheckRepository.getHomeInfo()

                // 2. 온보딩 정보 가져오기
                val memberProfile = authRepository.getMemberProfile()

                _uiState.value = _uiState.value.copy(
                    nickname = homeInfo.nickname,
                    email = homeInfo.email,
                    visaType = memberProfile.visaType,
                    koreanLevel = memberProfile.topikLevel,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                Timber.e(e, "프로필 로딩 실패")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.clearToken()
        }
    }
}