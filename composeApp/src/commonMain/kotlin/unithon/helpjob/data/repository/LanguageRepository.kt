package unithon.helpjob.data.repository

import kotlinx.coroutines.flow.Flow
import unithon.helpjob.data.model.AppLanguage

interface LanguageRepository {
    val currentLanguage: Flow<AppLanguage>
    suspend fun setLanguage(language: AppLanguage)
    suspend fun getCurrentLanguage(): AppLanguage
}
