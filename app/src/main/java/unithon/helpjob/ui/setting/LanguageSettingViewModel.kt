package unithon.helpjob.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.data.repository.LanguageRepository
import javax.inject.Inject

@HiltViewModel
class LanguageSettingViewModel @Inject constructor(
    private val languageRepository: LanguageRepository
) : ViewModel() {

    data class LanguageSettingUiState(
        val currentLanguage: AppLanguage = AppLanguage.KOREAN,
        val availableLanguages: List<AppLanguage> = AppLanguage.entries
    )

    private val _uiState = MutableStateFlow(LanguageSettingUiState())
    val uiState: StateFlow<LanguageSettingUiState> = _uiState.asStateFlow()

    init {
        loadCurrentLanguage()
    }

    private fun loadCurrentLanguage() {
        try {
            val currentLanguage = languageRepository.getCurrentLanguage()
            _uiState.value = _uiState.value.copy(currentLanguage = currentLanguage)
            Timber.d("🌐 현재 언어 로드: ${currentLanguage.displayName}")
        } catch (e: Exception) {
            Timber.e(e, "❌ 현재 언어 로드 실패")
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            try {
                Timber.d("🌐 언어 변경 시작: ${language.displayName}")

                languageRepository.setLanguage(language)
                _uiState.value = _uiState.value.copy(currentLanguage = language)

                Timber.d("✅ 언어 변경 완료: ${language.displayName}")

            } catch (e: Exception) {
                Timber.e(e, "❌ 언어 변경 실패: ${language.displayName}")
            }
        }
    }
}