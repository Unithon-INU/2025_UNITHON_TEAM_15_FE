package unithon.helpjob.ui.setting

import unithon.helpjob.data.repository.PolicyRepository
import unithon.helpjob.ui.base.BaseViewModel

class TermsOfServiceViewModel(
    private val policyRepository: PolicyRepository
) : BaseViewModel() {

    suspend fun getTermsOfService(): String {
        return try {
            policyRepository.getTermsOfService()
        } catch (e: Exception) {
            println("[TermsOfServiceViewModel] Failed to load terms of service: ${e.message}")
            ""
        }
    }
}
