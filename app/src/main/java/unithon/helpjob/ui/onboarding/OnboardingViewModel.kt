package unithon.helpjob.ui.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(

) : ViewModel() {
    data class OnboardingUiState(
        val language: String = "",
        val fullAgreement: Boolean = false,
        val serviceAgreement: Boolean = false,
        val privacyAgreement: Boolean = false,
        val ageAgreement: Boolean = false,
        val koreanLevel: String = "",
        val visa: String = "",
        val businesses: List<String> = emptyList(),
        val isLoading: Boolean = false,
        val userMessage: Int? = null,
        val isOnboardingSuccess: Boolean = false,
        val userProfileError: Boolean = false,
        val userProfileErrorMessage: Int? = null
    ) {
        val inLanguageValid: Boolean
            get() = language.isNotBlank()
        val isFullAgreementValid: Boolean
            get() = serviceAgreement && privacyAgreement && ageAgreement
        val isAllAgreementChecked: Boolean
            get() = serviceAgreement && privacyAgreement && ageAgreement
        val isKoreanLevelValid: Boolean
            get() = koreanLevel.isNotBlank()
        val isVisaValid: Boolean
            get() = visa.isNotBlank()
        val isBusinessValid: Boolean
            get() = businesses.isNotEmpty()
    }

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun updateLanguage(language: String) {
        _uiState.value = _uiState.value.copy(language = language)
    }

    fun updateAgreement(agreement: Boolean) {
        _uiState.value = _uiState.value.copy(
            fullAgreement = agreement,
            serviceAgreement = agreement,
            privacyAgreement = agreement,
            ageAgreement = agreement
        )
    }

    fun updateServiceAgreement(checked: Boolean) {
        val newState = _uiState.value.copy(serviceAgreement = checked)
        _uiState.value = newState.copy(
            fullAgreement = newState.isAllAgreementChecked
        )
    }

    fun updatePrivacyAgreement(checked: Boolean) {
        val newState = _uiState.value.copy(privacyAgreement = checked)
        _uiState.value = newState.copy(
            fullAgreement = newState.isAllAgreementChecked
        )
    }

    fun updateAgeAgreement(checked: Boolean) {
        val newState = _uiState.value.copy(ageAgreement = checked)
        _uiState.value = newState.copy(
            fullAgreement = newState.isAllAgreementChecked
        )
    }


    fun updateKoreanLevel(level: String) {
        _uiState.value = _uiState.value.copy(koreanLevel = level)
    }

    fun updateVisa(visa: String) {
        _uiState.value = _uiState.value.copy(visa = visa)
    }

    fun updateBusiness(business: String) {
        val currentBusinesses = _uiState.value.businesses.toMutableList()

        // 이미 선택된 업종이면 제거, 아니면 추가
        if (business in currentBusinesses) {
            currentBusinesses.remove(business)
        } else {
            currentBusinesses.add(business)
        }

        _uiState.value = _uiState.value.copy(businesses = currentBusinesses)
    }

    fun completeOnboarding() {
        _uiState.value = _uiState.value.copy(isOnboardingSuccess = true)
    }
}