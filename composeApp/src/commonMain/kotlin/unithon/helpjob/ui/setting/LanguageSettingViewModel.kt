package unithon.helpjob.ui.setting

import androidx.lifecycle.viewModelScope
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.language_change_failed
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.data.repository.LanguageRepository
import unithon.helpjob.ui.base.BaseViewModel
import unithon.helpjob.util.Logger

class LanguageSettingViewModel(
    private val languageRepository: LanguageRepository
) : BaseViewModel() {

    data class LanguageSettingUiState(
        val currentLanguage: AppLanguage = AppLanguage.KOREAN,
        val availableLanguages: List<AppLanguage> = AppLanguage.entries
    )

    private val _uiState = MutableStateFlow(LanguageSettingUiState())
    val uiState: StateFlow<LanguageSettingUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<StringResource>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {
        loadCurrentLanguage()
    }

    private fun loadCurrentLanguage() {
        viewModelScope.launch(crashPreventionHandler) {
            try {
                val currentLanguage = languageRepository.getCurrentLanguage()
                _uiState.value = _uiState.value.copy(currentLanguage = currentLanguage)
                Logger.d("[LanguageSettingViewModel]", "현재 언어 로드: ${currentLanguage.displayName}")
            } catch (e: Exception) {
                Logger.e("[LanguageSettingViewModel]", "현재 언어 로드 실패: ${e.message}")
            }
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch(crashPreventionHandler) {
            try {
                Logger.d("[LanguageSettingViewModel]", "언어 변경 시작: ${language.displayName}")
                languageRepository.setLanguage(language)
                _uiState.value = _uiState.value.copy(currentLanguage = language)
                Logger.d("[LanguageSettingViewModel]", "언어 변경 완료: ${language.displayName}")
            } catch (e: Exception) {
                Logger.e("[LanguageSettingViewModel]", "언어 변경 실패: ${e.message}")
                _snackbarMessage.emit(Res.string.language_change_failed)
            }
        }
    }
}
