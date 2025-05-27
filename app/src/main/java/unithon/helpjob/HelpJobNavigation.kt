// HelpJobNavigation.kt
package unithon.helpjob

import androidx.navigation.NavHostController

/**
 * Navigation 관련 화면들의 이름
 */
object HelpJobScreens {
    const val SIGN_IN_SCREEN = "sign_in"
    const val SIGN_UP_SCREEN = "sign_up"
    const val NICKNAME_SETUP_SCREEN = "nickname_setup"
    const val ONBOARDING_SCREEN = "onboarding"
}

/**
 * Destinations (라우트 정의)
 */
object HelpJobDestinations {
    const val SIGN_IN_ROUTE = HelpJobScreens.SIGN_IN_SCREEN
    const val SIGN_UP_ROUTE = HelpJobScreens.SIGN_UP_SCREEN
    const val NICKNAME_SETUP_ROUTE = HelpJobScreens.NICKNAME_SETUP_SCREEN
    const val ONBOARDING_ROUTE = HelpJobScreens.ONBOARDING_SCREEN
}

/**
 * Navigation 동작들 - 완전히 일관성 있는 패턴
 */
class HelpJobNavigationActions(private val navController: NavHostController) {

    // 인증 관련 네비게이션 (기존)
    fun navigateToSignIn() {
        navController.navigate(HelpJobDestinations.SIGN_IN_ROUTE) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    fun navigateToSignUp() {
        navController.navigate(HelpJobDestinations.SIGN_UP_ROUTE)
    }

    fun navigateToNicknameSetup() {
        navController.navigate(HelpJobDestinations.NICKNAME_SETUP_ROUTE) {
            popUpTo(HelpJobDestinations.SIGN_UP_ROUTE) { inclusive = true }
        }
    }

    fun navigateToOnboarding() {
        navController.navigate(HelpJobDestinations.ONBOARDING_ROUTE) {
            popUpTo(HelpJobDestinations.NICKNAME_SETUP_ROUTE) { inclusive = true }
        }
    }

    // 하단 탭 네비게이션 - 기존 패턴과 완전히 일관성 있게 구성
    fun navigateToHome() {
        navController.navigate(BottomNavDestination.HOME.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToCalculate() {
        navController.navigate(BottomNavDestination.CALCULATE.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToContent() {
        navController.navigate(BottomNavDestination.CONTENT.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToProfile() {
        navController.navigate(BottomNavDestination.PROFILE.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}