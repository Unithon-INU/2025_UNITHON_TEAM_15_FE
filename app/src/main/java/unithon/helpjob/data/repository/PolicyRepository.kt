package unithon.helpjob.data.repository

interface PolicyRepository {
    suspend fun getPrivacyPolicy(): String
    suspend fun getTermsOfService(): String
}