package unithon.helpjob.ui.profile

import androidx.lifecycle.viewModelScope
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.error_profile_load_failed
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import unithon.helpjob.util.Logger
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.ui.base.BaseViewModel

data class ProfileUiState(
    val visaType: String? = null,
    val languageLevel: String? = null,
    val industry: String? = null,
    val isLoading: Boolean = false,
    val isGuest: Boolean = false  // 🆕 Guest Mode 여부
)

class ProfileViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<StringResource>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {
        // 🆕 Guest Mode 실시간 구독 (로그인/로그아웃 시 자동 갱신)
        viewModelScope.launch {
            authRepository.observeGuestMode()
                .distinctUntilChanged()  // 🔑 실제로 값이 변경될 때만
                .collect { isGuest ->
                    _uiState.value = _uiState.value.copy(isGuest = isGuest)

                    if (!isGuest) {
                        // Member로 전환 시 프로필 로드
                        loadUserProfile()
                    }
                }
        }
    }

    fun refreshProfile() {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch(crashPreventionHandler) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val memberProfile = authRepository.getMemberProfile()

                _uiState.value = _uiState.value.copy(
                    visaType = memberProfile.visaType,
                    languageLevel = memberProfile.languageLevel,
                    industry = memberProfile.industry,
                    isLoading = false
                )
            } catch (e: Exception) {
                Logger.e(e, "프로필 로딩 실패")
                _snackbarMessage.emit(Res.string.error_profile_load_failed)  // 이 줄 추가
                _uiState.value = _uiState.value.copy(
                    isLoading = false
                )
            }
        }
    }
}