package unithon.helpjob.ui.splash

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.ui.base.BaseViewModel

class SplashViewModel(
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState = _uiState.asStateFlow()

    init {
        checkAppState()
    }

    private fun checkAppState() {
        viewModelScope.launch(crashPreventionHandler) {
            // 최소 1.5초는 스플래시 보여주기
            val minSplashTime = async { delay(1500) }

            // 동시에 앱 상태 체크
            val appStateCheck = async {
                val token = authRepository.getToken()

                when {
                    token == null -> NavigationTarget.Login
                    else -> {
                        try {
                            // 🆕 토큰 유효성을 먼저 체크
                            val profile = authRepository.getMemberProfile()

                            // 토큰이 유효하면 온보딩 완료 여부 판단
                            if (profile.language.isNotEmpty() &&
                                profile.visaType.isNotEmpty() &&
                                profile.topikLevel.isNotEmpty() &&
                                profile.industry.isNotEmpty()) {
                                NavigationTarget.Main
                            } else {
                                NavigationTarget.Onboarding
                            }
                        } catch (e: Exception) {
                            // 모든 예외: 로그인으로
                            Timber.e(e, "프로필 조회 실패 - 로그인으로 이동")
                            authRepository.clearToken()  // 토큰도 클리어
                            NavigationTarget.Login
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
    data object Login : NavigationTarget()
    data object Onboarding : NavigationTarget()
    data object Main : NavigationTarget()
}