package unithon.helpjob.ui.setting

import unithon.helpjob.data.repository.PolicyRepository
import unithon.helpjob.ui.base.BaseViewModel

class PrivacyPolicyViewModel(
    private val policyRepository: PolicyRepository
) : BaseViewModel() {

    suspend fun getPrivacyPolicy(): String {
        return try {
            policyRepository.getPrivacyPolicy()
        } catch (e: Exception) {
            println("[PrivacyPolicyViewModel] Failed to load privacy policy: ${e.message}")
            ""
        }
    }
}
