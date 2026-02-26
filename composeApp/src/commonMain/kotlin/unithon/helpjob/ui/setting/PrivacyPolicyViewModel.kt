package unithon.helpjob.ui.setting

import unithon.helpjob.data.repository.PolicyRepository
import unithon.helpjob.ui.base.BaseViewModel
import unithon.helpjob.util.Logger

class PrivacyPolicyViewModel(
    private val policyRepository: PolicyRepository
) : BaseViewModel() {

    suspend fun getPrivacyPolicy(): String {
        return try {
            policyRepository.getPrivacyPolicy()
        } catch (e: Exception) {
            Logger.e("[PrivacyPolicyViewModel]", "Failed to load privacy policy: ${e.message}")
            ""
        }
    }
}
