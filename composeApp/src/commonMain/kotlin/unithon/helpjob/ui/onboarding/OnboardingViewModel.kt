package unithon.helpjob.ui.onboarding

import androidx.lifecycle.viewModelScope
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.error_authentication_required
import helpjob.composeapp.generated.resources.onboarding_error_message
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import unithon.helpjob.util.Logger
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.data.model.Business
import unithon.helpjob.data.model.EnglishLevel
import unithon.helpjob.data.model.GuestProfile
import unithon.helpjob.data.model.LanguageTrack
import unithon.helpjob.data.model.TopikLevel
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.LanguageRepository
import unithon.helpjob.data.repository.UnauthorizedException
import unithon.helpjob.ui.base.BaseViewModel

class OnboardingViewModel(
    private val authRepository: AuthRepository,
    private val languageRepository: LanguageRepository
) : BaseViewModel() {
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
        val selectedBusinesses: List<Business> = emptyList(),
        val selectedTrack: LanguageTrack? = null,
        val englishLevel: String = "",
        val selectedEnglishLevel: EnglishLevel? = null
    ) {
        val inLanguageValid: Boolean
            get() = language.isNotBlank()
        val isFullAgreementValid: Boolean
            get() = serviceAgreement && privacyAgreement && ageAgreement
        val isAllAgreementChecked: Boolean
            get() = serviceAgreement && privacyAgreement && ageAgreement
        val isTrackValid: Boolean
            get() = selectedTrack != null
        val isLanguageLevelValid: Boolean
            get() = when (selectedTrack) {
                LanguageTrack.KOREAN -> selectedTopikLevel != null
                LanguageTrack.ENGLISH -> selectedEnglishLevel != null
                null -> false
            }
        val isVisaValid: Boolean
            get() = visa.isNotBlank()
        val isBusinessValid: Boolean
            get() = businesses.isNotEmpty()
        val isAllChecked: Boolean
            get() = inLanguageValid && isFullAgreementValid && isTrackValid && isLanguageLevelValid && isVisaValid && isBusinessValid
    }

    private val _uiState = MutableStateFlow(
        OnboardingUiState(
            // 🔥 초기화 시 GlobalLanguageState에서 현재 언어를 가져와 설정
            language = unithon.helpjob.data.repository.GlobalLanguageState.currentLanguage.value.displayName
        )
    )
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<StringResource>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    fun updateLanguage(language: String) {
        Logger.d("🌐 언어 업데이트 시작: $language")

        val selectedLanguage = AppLanguage.fromDisplayName(language)
        Logger.d("선택된 언어: ${selectedLanguage.displayName} (${selectedLanguage.code})")

        // UI 상태 즉시 업데이트
        _uiState.value = _uiState.value.copy(language = language)

        viewModelScope.launch(crashPreventionHandler) {
            try {
                // 🆕 AppLocaleManager로 언어 설정 (즉시 적용됨)
                languageRepository.setLanguage(selectedLanguage)

                Logger.d("✅ 언어 설정 완료: ${selectedLanguage.code}")

            } catch (e: Exception) {
                Logger.e(e, "❌ 언어 설정 실패")
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


    fun updateTrack(track: LanguageTrack) {
        _uiState.update {
            it.copy(
                selectedTrack = track,
                // 트랙 전환 시 이전 선택 초기화
                koreanLevel = "",
                selectedTopikLevel = null,
                englishLevel = "",
                selectedEnglishLevel = null
            )
        }
    }

    fun updateKoreanLevel(level: String) {
        val topikLevel = TopikLevel.fromDisplayText(level)
        _uiState.value = _uiState.value.copy(
            koreanLevel = level,
            selectedTopikLevel = topikLevel
        )
    }

    fun updateEnglishLevel(level: String) {
        val englishLevel = EnglishLevel.fromDisplayText(level)
        _uiState.update {
            it.copy(
                englishLevel = level,
                selectedEnglishLevel = englishLevel
            )
        }
    }

    fun updateVisa(visa: String) {
        _uiState.value = _uiState.value.copy(visa = visa)
    }

    fun updateBusiness(business: String) {
        Logger.d("🏢 업무 직종 업데이트: $business")

        val currentBusinesses = _uiState.value.businesses.toMutableList()
        val currentIndustries = _uiState.value.selectedBusinesses.toMutableList()

        if (business in currentBusinesses) {
            // 이미 선택된 업종이면 제거
            currentBusinesses.remove(business)

            // Business enum에서도 제거
            val industryToRemove = Business.fromDisplayText(business)
            if (industryToRemove != null) {
                currentIndustries.remove(industryToRemove)
                Logger.d("업종 제거: ${industryToRemove.name} -> API값: ${industryToRemove.apiValueKo}")
            }
        } else {
            // 새로운 업종 추가
            currentBusinesses.add(business)

            // Business enum에도 추가
            val industryToAdd = Business.fromDisplayText(business)
            if (industryToAdd != null && industryToAdd !in currentIndustries) {
                currentIndustries.add(industryToAdd)
                Logger.d("업종 추가: ${industryToAdd.name} -> API값: ${industryToAdd.apiValueKo}")
            }
        }

        _uiState.value = _uiState.value.copy(
            businesses = currentBusinesses,  // UI 표시용
            selectedBusinesses = currentIndustries  // enum 저장
        )

        // 현재 선택된 업종들의 API 값 로그
        val apiValues = Business.toApiValues(currentIndustries)
        Logger.d("현재 선택된 업종 API 값: $apiValues")
    }

    fun completeOnboarding() {
        if (!uiState.value.isAllChecked) return

        viewModelScope.launch(crashPreventionHandler) {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val isGuest = authRepository.isGuestMode()

                val languageLevelValue = when (_uiState.value.selectedTrack) {
                    LanguageTrack.KOREAN -> _uiState.value.selectedTopikLevel?.apiValue ?: "없음"
                    LanguageTrack.ENGLISH -> _uiState.value.selectedEnglishLevel?.apiValue ?: "자격증 없음"
                    null -> "없음"
                }

                if (isGuest) {
                    val guestProfile = GuestProfile(
                        language = _uiState.value.language,
                        languageLevel = languageLevelValue,
                        visaType = _uiState.value.visa,
                        industry = Business.toApiValues(_uiState.value.selectedBusinesses)
                    )

                    authRepository.saveGuestProfile(guestProfile)
                    Logger.d("✅ Guest 프로필 로컬 저장 완료")

                } else {
                    authRepository.setProfile(
                        language = _uiState.value.language,
                        languageLevel = languageLevelValue,
                        visaType = _uiState.value.visa,
                        industry = Business.toApiValues(_uiState.value.selectedBusinesses)
                    )
                    Logger.d("✅ Member 프로필 서버 저장 완료")
                }

                _uiState.update {
                    it.copy(
                        language = "",
                        koreanLevel = "",
                        visa = "",
                        businesses = emptyList(),
                        isOnboardingSuccess = true,
                        isLoading = false,
                        userProfileError = false,
                        userProfileErrorMessage = null
                    )
                }

            } catch (e: UnauthorizedException) {
                Logger.e(e, "인증 오류 발생")
                _snackbarMessage.emit(Res.string.error_authentication_required)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                Logger.e(e, "프로필 설정 오류 발생")
                _snackbarMessage.emit(Res.string.onboarding_error_message)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}