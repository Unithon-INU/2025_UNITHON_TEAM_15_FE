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
import timber.log.Timber
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.data.repository.LanguageRepository
import unithon.helpjob.ui.base.BaseViewModel

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
        try {
            val currentLanguage = languageRepository.getCurrentLanguage()
            _uiState.value = _uiState.value.copy(currentLanguage = currentLanguage)
            Timber.d("🌐 현재 언어 로드: ${currentLanguage.displayName}")
        } catch (e: Exception) {
            Timber.e(e, "❌ 현재 언어 로드 실패")
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch(crashPreventionHandler) {
            try {
                Timber.d("🌐 언어 변경 시작: ${language.displayName}")

                languageRepository.setLanguage(language)
                _uiState.value = _uiState.value.copy(currentLanguage = language)

                Timber.d("✅ 언어 변경 완료: ${language.displayName}")

            } catch (e: Exception) {
                Timber.e(e, "❌ 언어 변경 실패: ${language.displayName}")
                _snackbarMessage.emit(Res.string.language_change_failed)
            }
        }
    }
}