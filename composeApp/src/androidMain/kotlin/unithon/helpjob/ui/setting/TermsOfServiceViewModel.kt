package unithon.helpjob.ui.setting

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import unithon.helpjob.data.repository.PolicyRepository
import unithon.helpjob.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class TermsOfServiceViewModel @Inject constructor(
    private val repository: PolicyRepository
) : BaseViewModel() {

    private val _htmlContent = MutableStateFlow<String?>(null)
    val htmlContent = _htmlContent.asStateFlow()

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch(crashPreventionHandler) {
            _htmlContent.value = repository.getTermsOfService()
        }
    }
}