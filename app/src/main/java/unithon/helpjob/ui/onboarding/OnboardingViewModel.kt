package unithon.helpjob.ui.onboarding

import TopikLevel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import unithon.helpjob.R
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.data.model.Business
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.LanguageRepository
import unithon.helpjob.data.repository.UnauthorizedException
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val languageRepository: LanguageRepository
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
        val userProfileErrorMessage: Int? = null,
        val selectedTopikLevel: TopikLevel? = null,
        val selectedBusinesses: List<Business> = emptyList()
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
        val isAllChecked: Boolean
            get() = inLanguageValid && isFullAgreementValid && isKoreanLevelValid && isVisaValid && isBusinessValid
    }

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _snackBarMessageResId = MutableSharedFlow<Int>()
    val snackBarMessageResId = _snackBarMessageResId.asSharedFlow()

    fun updateLanguage(language: String) {
        viewModelScope.launch {
            Timber.d("🌐 언어 업데이트 시작: $language")

            val selectedLanguage = AppLanguage.fromDisplayName(language)
            Timber.d("선택된 언어: ${selectedLanguage.displayName} (${selectedLanguage.code})")

            // UI 상태 즉시 업데이트
            _uiState.value = _uiState.value.copy(language = language)

            try {
                // 🆕 AppLocaleManager로 언어 설정 (즉시 적용됨)
                languageRepository.setLanguage(selectedLanguage)

                Timber.d("✅ 언어 설정 완료: ${selectedLanguage.code}")

            } catch (e: Exception) {
                Timber.e(e, "❌ 언어 설정 실패")
            }
        }
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
        val topikLevel = TopikLevel.fromDisplayText(level)
        _uiState.value = _uiState.value.copy(
            koreanLevel = level,  // UI 표시용 텍스트
            selectedTopikLevel = topikLevel  // 🆕 enum 저장
        )
    }

    fun updateVisa(visa: String) {
        _uiState.value = _uiState.value.copy(visa = visa)
    }

    fun updateBusiness(business: String) {
        Timber.d("🏢 업무 직종 업데이트: $business")

        val currentBusinesses = _uiState.value.businesses.toMutableList()
        val currentIndustries = _uiState.value.selectedBusinesses.toMutableList()

        if (business in currentBusinesses) {
            // 이미 선택된 업종이면 제거
            currentBusinesses.remove(business)

            // Business enum에서도 제거
            val industryToRemove = Business.fromDisplayText(business)
            if (industryToRemove != null) {
                currentIndustries.remove(industryToRemove)
                Timber.d("업종 제거: ${industryToRemove.name} -> API값: ${industryToRemove.apiValue}")
            }
        } else {
            // 새로운 업종 추가
            currentBusinesses.add(business)

            // Business enum에도 추가
            val industryToAdd = Business.fromDisplayText(business)
            if (industryToAdd != null && industryToAdd !in currentIndustries) {
                currentIndustries.add(industryToAdd)
                Timber.d("업종 추가: ${industryToAdd.name} -> API값: ${industryToAdd.apiValue}")
            }
        }

        _uiState.value = _uiState.value.copy(
            businesses = currentBusinesses,  // UI 표시용
            selectedBusinesses = currentIndustries  // enum 저장
        )

        // 현재 선택된 업종들의 API 값 로그
        val apiValues = Business.toApiValues(currentIndustries)
        Timber.d("현재 선택된 업종 API 값: $apiValues")
    }

    fun completeOnboarding() {
        if (!uiState.value.isAllChecked) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.setProfile(
                    language = _uiState.value.language,
                    topikLevel = _uiState.value.selectedTopikLevel?.apiValue ?: "없음",
                    visaType = _uiState.value.visa,
                    industry = Business.toApiValues(_uiState.value.selectedBusinesses)
                )

                _uiState.update {
                    it.copy(
                        language = "",
                        koreanLevel = "",
                        visa = "",
                        businesses = emptyList(),
                        isOnboardingSuccess = true,
                        isLoading = false,  // 로딩 상태 해제
                        userProfileError = false,  // 에러 상태 초기화
                        userProfileErrorMessage = null  // 에러 메시지 초기화
                    )
                }

            } catch (e: UnauthorizedException){
                Timber.e(e, "인증 오류 발생")

                _snackBarMessageResId.emit(R.string.error_authentication_required)
                _uiState.update {
                    it.copy(
                        isLoading = false  // 로딩 상태 해제
                    )
                }
            } catch (e: Exception) {  // 다른 예외 처리 추가
                Timber.e(e, "프로필 설정 오류 발생")

                _snackBarMessageResId.emit(R.string.onboarding_error_message)
                _uiState.update {
                    it.copy(
                        isLoading = false  // 로딩 상태 해제
                    )
                }
            }

        }
    }
}