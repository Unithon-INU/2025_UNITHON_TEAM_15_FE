package unithon.helpjob

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavHostController

/**
 * Navigation 관련 화면들의 이름
 */
object HelpJobScreens {
    const val SPLASH_SCREEN = "splash"
    const val SIGN_IN_SCREEN = "sign_in"
    const val SIGN_UP_SCREEN = "sign_up"
    const val NICKNAME_SETUP_SCREEN = "nickname_setup"
    const val SIGN_UP_SUCCESS_SCREEN  = "signup_success"
    const val ONBOARDING_SCREEN = "onboarding"
    const val MAIN_SCREEN = "main"
    const val CALCULATOR = "calculator"
    const val STEP_DETAIL_SCREEN = "step_detail"
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
    const val SPLASH_ROUTE = HelpJobScreens.SPLASH_SCREEN
    const val SIGN_IN_ROUTE = HelpJobScreens.SIGN_IN_SCREEN
    const val SIGN_UP_ROUTE = HelpJobScreens.SIGN_UP_SCREEN
    const val NICKNAME_SETUP_ROUTE = HelpJobScreens.NICKNAME_SETUP_SCREEN
    const val SIGN_UP_SUCCESS_ROUTE  = HelpJobScreens.SIGN_UP_SUCCESS_SCREEN
    const val ONBOARDING_ROUTE = HelpJobScreens.ONBOARDING_SCREEN
    const val MAIN_ROUTE = HelpJobScreens.MAIN_SCREEN
    const val CALCULATOR = HelpJobScreens.CALCULATOR
    const val STEP_DETAIL_ROUTE = "step_detail/{stepId}"
}

/**
 * 하단바 탭 정의 (경로 + 메타데이터 통합 관리)
 * 하단바 관련 문제는 여기서만 확인하면 됨
 */
enum class BottomNavDestination(
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val iconTextId: Int,
    val route: String,
) {
    HOME(
        selectedIcon = R.drawable.home_selected,
        unselectedIcon = R.drawable.home_unselected,
        iconTextId = R.string.bottom_nav_home,
        route = "home",
    ),
    CALCULATE(
        selectedIcon = R.drawable.calculate_selected,
        unselectedIcon = R.drawable.calculate_unselected,
        iconTextId = R.string.bottom_nav_calculate,
        route = "calculate",
    ),
    CONTENT(
        selectedIcon = R.drawable.content_selected,
        unselectedIcon = R.drawable.content_unselected,
        iconTextId = R.string.bottom_nav_content,
        route = "content",
    ),
    PROFILE(
        selectedIcon = R.drawable.profile_selected,
        unselectedIcon = R.drawable.profile_unselected,
        iconTextId = R.string.bottom_nav_profile,
        route = "profile",
    );

    companion object {
        /**
         * 하단바 탭 경로인지 확인
         */
        fun isBottomTabRoute(route: String?): Boolean {
            return entries.any { it.route == route }
        }

        /**
         * 하단바 기본 시작 화면
         */
        val DEFAULT_TAB = HOME
    }
}

/**
 * 모든 네비게이션 동작 관리
 */
class HelpJobNavigationActions(private val navController: NavHostController) {

    // 인증 플로우 - 이전 화면 제거하며 진행
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

    fun navigateToSignUpSuccess() {
        navController.navigate(HelpJobDestinations.SIGN_UP_SUCCESS_ROUTE) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    fun navigateToOnboarding() {
        navController.navigate(HelpJobDestinations.ONBOARDING_ROUTE) {
            popUpTo(HelpJobDestinations.NICKNAME_SETUP_ROUTE) { inclusive = true }
        }
    }

    /**
     * 하단바 탭 네비게이션 - 통합된 함수로 중복 제거
     * Now in Android의 navigateToTopLevelDestination 패턴 적용
     */
    fun navigateToBottomTab(destination: BottomNavDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToCalculator() {
        navController.navigate(HelpJobDestinations.CALCULATOR) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    fun navigateToStepDetail(stepId: Int) {
        navController.navigate("step_detail/$stepId")
    }
}