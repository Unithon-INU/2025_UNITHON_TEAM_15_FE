package unithon.helpjob.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import unithon.helpjob.data.model.response.MemberProfileGetRes
import unithon.helpjob.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState = _uiState.asStateFlow()

    init {
        checkAppState()
    }

    private fun checkAppState() {
        viewModelScope.launch {
            // 최소 1.5초는 스플래시 보여주기
            val minSplashTime = async { delay(1500) }

            // 동시에 앱 상태 체크
            val appStateCheck = async {
                val token = authRepository.getToken()

                when {
                    token == null -> NavigationTarget.Login
                    else -> {
                        if (authRepository.isOnboardingCompleted()) {
                            NavigationTarget.Main
                        } else {
                            NavigationTarget.Onboarding
                        }
                    }
                }
            }

            // 둘 다 완료되면 분기
            minSplashTime.await()
            val target = appStateCheck.await()
            _uiState.value = _uiState.value.copy(navigationTarget = target)
        }
    }
}

data class SplashUiState(
    val navigationTarget: NavigationTarget? = null
)

sealed class NavigationTarget {
    object Login : NavigationTarget()
    object Onboarding : NavigationTarget()
    object Main : NavigationTarget()
}