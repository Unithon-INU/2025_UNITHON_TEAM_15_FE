package unithon.helpjob.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unithon.helpjob.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            authRepository.clearToken()
        }
    }
}