package unithon.helpjob.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.LanguageRepository
import unithon.helpjob.data.repository.UnauthorizedException
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val languageRepository: LanguageRepository
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
                        } catch (e: UnauthorizedException) {
                            // 토큰이 무효한 경우 토큰 클리어 후 로그인으로 분기
                            authRepository.clearToken()
                            NavigationTarget.Login
                        } catch (e: Exception) {
                            // 기타 예외는 온보딩으로 처리
                            Timber.e(e, "프로필 조회 실패")
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