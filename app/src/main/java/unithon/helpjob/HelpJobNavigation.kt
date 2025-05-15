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
    const val MAIN_SCREEN = "main"
}

/**
 * Navigation에서 사용되는 모든 인자들의 키 값을 정의
 * - Path Parameter: 필수 인자 (예: /user/{userId})
 * - Query Parameter: 선택적 인자 (예: ?userMessage={userMessage})
 */
//object HelpJobDestinationsArgs {
//    const val USER_ID_ARG = "userId"
//}

/**
 * Destinations (라우트 정의)
 */
object HelpJobDestinations {
    const val SIGN_IN_ROUTE = HelpJobScreens.SIGN_IN_SCREEN
    const val SIGN_UP_ROUTE = HelpJobScreens.SIGN_UP_SCREEN
    const val NICKNAME_SETUP_ROUTE = HelpJobScreens.NICKNAME_SETUP_SCREEN
    const val ONBOARDING_ROUTE = HelpJobScreens.ONBOARDING_SCREEN
    const val MAIN_ROUTE = HelpJobScreens.MAIN_SCREEN
}

/**
 * Navigation 동작들
 */
class HelpJobNavigationActions(private val navController: NavHostController) {

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
            popUpTo(HelpJobDestinations.SIGN_IN_ROUTE) { inclusive = true }
        }
    }

    fun navigateToMain() {
        navController.navigate(HelpJobDestinations.MAIN_ROUTE) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }
}