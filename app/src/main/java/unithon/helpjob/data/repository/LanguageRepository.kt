package unithon.helpjob.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import unithon.helpjob.data.model.AppLanguage
import javax.inject.Inject
import javax.inject.Singleton

// 굳이 저장/불러오기만 있는 거 말고 복잡한 로직이 없어서 이건 인터페이스 없이 바로 구현했어요

@Singleton
class LanguageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val languageKey = stringPreferencesKey("app_language")

    suspend fun getLanguage(): AppLanguage {
        return context.dataStore.data
            .map { preferences ->
                val savedCode = preferences[languageKey]
                if (savedCode != null) {
                    AppLanguage.fromCode(savedCode)
                } else {
                    throw NoSuchElementException("No saved language preference")
                }
            }
            .firstOrNull() ?: throw NoSuchElementException("No saved language preference")
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[languageKey] = language.code
        }
        applyLanguage(language)
    }

    private fun applyLanguage(language: AppLanguage) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(language.code)
        )
    }
}