package unithon.helpjob.ui.profile.edit

import androidx.lifecycle.viewModelScope
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.error_profile_edit_failed
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import unithon.helpjob.data.model.Business
import unithon.helpjob.data.model.EnglishLevel
import unithon.helpjob.data.model.LanguageTrack
import unithon.helpjob.data.model.ProfileField
import unithon.helpjob.data.model.TopikLevel
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.ui.base.BaseViewModel
import unithon.helpjob.util.Logger

data class ProfileEditUiState(
    val profileField: ProfileField = ProfileField.VISA_TYPE,

    // VISA_TYPE
    val selectedVisa: String = "",

    // LANGUAGE_LEVEL (2단계 플로우)
    val isTrackStep: Boolean = true,
    val selectedTrack: LanguageTrack? = null,
    val selectedTopikLevel: TopikLevel? = null,
    val selectedEnglishLevel: EnglishLevel? = null,

    // INDUSTRY
    val selectedBusinesses: List<Business> = emptyList(),

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
) {
    val isValid: Boolean
        get() = when (profileField) {
            ProfileField.VISA_TYPE -> selectedVisa.isNotEmpty()
            ProfileField.LANGUAGE_LEVEL -> when (selectedTrack) {
                LanguageTrack.KOREAN -> selectedTopikLevel != null
                LanguageTrack.ENGLISH -> selectedEnglishLevel != null
                null -> false
            }
            ProfileField.INDUSTRY -> selectedBusinesses.isNotEmpty()
        }

    /** 현재 트랙 단계에서의 유효성 (다음 버튼 활성화 조건) */
    val isTrackValid: Boolean
        get() = selectedTrack != null

    val apiValue: String
        get() = when (profileField) {
            ProfileField.VISA_TYPE -> selectedVisa
            ProfileField.LANGUAGE_LEVEL -> when (selectedTrack) {
                LanguageTrack.KOREAN -> selectedTopikLevel?.apiValue ?: ""
                LanguageTrack.ENGLISH -> selectedEnglishLevel?.apiValue ?: ""
                null -> ""
            }
            ProfileField.INDUSTRY -> Business.toApiValues(selectedBusinesses)
        }
}

class ProfileEditViewModel(
    private val profileField: ProfileField,
    private val currentValue: String,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ProfileEditUiState(profileField = profileField))
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<StringResource>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {
        initializeFromCurrentValue()
    }

    private fun initializeFromCurrentValue() {
        if (currentValue.isEmpty()) return

        when (profileField) {
            ProfileField.VISA_TYPE -> {
                _uiState.update { it.copy(selectedVisa = currentValue) }
            }
            ProfileField.LANGUAGE_LEVEL -> {
                // apiValue로부터 트랙과 레벨 추론
                val topikLevel = TopikLevel.entries.find { it.apiValue == currentValue }
                val englishLevel = EnglishLevel.fromApiValue(currentValue)

                when {
                    topikLevel != null -> _uiState.update {
                        it.copy(
                            selectedTrack = LanguageTrack.KOREAN,
                            selectedTopikLevel = topikLevel
                        )
                    }
                    englishLevel != null -> _uiState.update {
                        it.copy(
                            selectedTrack = LanguageTrack.ENGLISH,
                            selectedEnglishLevel = englishLevel
                        )
                    }
                }
            }
            ProfileField.INDUSTRY -> {
                val businesses = currentValue.split(",")
                    .map { it.trim() }
                    .mapNotNull { Business.fromApiValue(it) }
                _uiState.update { it.copy(selectedBusinesses = businesses) }
            }
        }
    }

    fun selectVisa(visa: String) {
        _uiState.update { it.copy(selectedVisa = visa) }
    }

    fun selectTrack(track: LanguageTrack) {
        _uiState.update {
            it.copy(
                selectedTrack = track,
                selectedTopikLevel = null,
                selectedEnglishLevel = null,
                isTrackStep = false
            )
        }
    }

    fun goBackToTrackStep() {
        _uiState.update { it.copy(isTrackStep = true) }
    }

    fun selectTopikLevel(level: TopikLevel) {
        _uiState.update { it.copy(selectedTopikLevel = level) }
    }

    fun selectEnglishLevel(level: EnglishLevel) {
        _uiState.update { it.copy(selectedEnglishLevel = level) }
    }

    fun toggleBusiness(business: Business) {
        _uiState.update { state ->
            val updated = state.selectedBusinesses.toMutableList()
            if (business in updated) updated.remove(business) else updated.add(business)
            state.copy(selectedBusinesses = updated)
        }
    }

    fun save() {
        if (!uiState.value.isValid) return

        viewModelScope.launch(crashPreventionHandler) {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.patchProfileField(
                    profileField = profileField.name,
                    value = uiState.value.apiValue
                )
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                Logger.e(e, "프로필 필드 수정 실패: ${profileField.name}")
                _snackbarMessage.emit(Res.string.error_profile_edit_failed)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
