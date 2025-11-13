package unithon.helpjob.data.repository

import kotlinx.coroutines.flow.Flow
import unithon.helpjob.data.model.AppLanguage

interface LanguageRepository {
    val currentLanguage: Flow<AppLanguage>
    fun setLanguage(language: AppLanguage)
    fun getCurrentLanguage(): AppLanguage
}
