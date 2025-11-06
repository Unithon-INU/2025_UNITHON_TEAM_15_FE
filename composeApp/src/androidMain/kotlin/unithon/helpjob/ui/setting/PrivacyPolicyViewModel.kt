package unithon.helpjob.ui.setting

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import unithon.helpjob.data.repository.PolicyRepository
import unithon.helpjob.ui.base.BaseViewModel

class PrivacyPolicyViewModel(
    private val repository: PolicyRepository
) : BaseViewModel() {

    private val _htmlContent = MutableStateFlow<String?>(null)
    val htmlContent = _htmlContent.asStateFlow()

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch(crashPreventionHandler) {
            _htmlContent.value = repository.getPrivacyPolicy()
        }
    }
}