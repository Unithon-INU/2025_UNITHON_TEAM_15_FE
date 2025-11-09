package unithon.helpjob.data.repository

import unithon.helpjob.data.model.AppLanguage

interface LanguageRepository {
    fun setLanguage(language: AppLanguage)
    fun getCurrentLanguage(): AppLanguage
}
