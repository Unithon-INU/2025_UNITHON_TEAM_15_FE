package unithon.helpjob.data.repository

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.util.Logger

/**
 * 앱 전역 언어 상태 관리
 *
 * LocalAppLocale과 함께 사용하여 런타임 언어 변경 지원
 */
object GlobalLanguageState {
    private val _currentLanguage = mutableStateOf(AppLanguage.ENGLISH)
    val currentLanguage: State<AppLanguage> = _currentLanguage

    fun updateLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        Logger.d("GlobalLanguageState", "전역 언어 상태 업데이트: ${language.displayName}")
    }

    fun initializeLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        Logger.d("GlobalLanguageState", "전역 언어 초기화: ${language.displayName}")
    }
}
