package unithon.helpjob

import androidx.navigation.NavHostController
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.bottom_nav_calculate
import helpjob.composeapp.generated.resources.bottom_nav_content
import helpjob.composeapp.generated.resources.bottom_nav_home
import helpjob.composeapp.generated.resources.bottom_nav_profile
import helpjob.composeapp.generated.resources.calculate_selected
import helpjob.composeapp.generated.resources.calculate_unselected
import helpjob.composeapp.generated.resources.content_selected
import helpjob.composeapp.generated.resources.content_unselected
import helpjob.composeapp.generated.resources.home_selected
import helpjob.composeapp.generated.resources.home_unselected
import helpjob.composeapp.generated.resources.profile_selected
import helpjob.composeapp.generated.resources.profile_unselected
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import unithon.helpjob.HelpJobDestinations.STEP_DETAIL_ROUTE

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
    const val STEP_DETAIL_SCREEN = "step_detail"
    const val SETTING_SCREEN = "setting"
    const val LANGUAGE_SETTING_SCREEN = "language_setting"
    const val PRIVACY_POLICY_SCREEN = "privacy_policy"
    const val TERMS_OF_SERVICE_SCREEN = "terms_of_service"
}

/**
 * Destinations (라우트 정의)
 */
object HelpJobDestinations {
    // 🔥 NavHost의 root graph route (모든 화면이 HomeViewModel을 공유하기 위한 스코프)
    const val ROOT_GRAPH_ROUTE = "root_graph"

    // 🏠 Main 화면들의 nested navigation graph (BottomNav 영역)
    const val MAIN_GRAPH_ROUTE = "main_graph"

    const val SPLASH_ROUTE = HelpJobScreens.SPLASH_SCREEN
    const val SIGN_IN_ROUTE = HelpJobScreens.SIGN_IN_SCREEN
    const val SIGN_UP_ROUTE = HelpJobScreens.SIGN_UP_SCREEN
    const val NICKNAME_SETUP_ROUTE = HelpJobScreens.NICKNAME_SETUP_SCREEN
    const val SIGN_UP_SUCCESS_ROUTE  = HelpJobScreens.SIGN_UP_SUCCESS_SCREEN
    const val ONBOARDING_ROUTE = HelpJobScreens.ONBOARDING_SCREEN
    const val STEP_DETAIL_ROUTE = HelpJobScreens.STEP_DETAIL_SCREEN
    const val SETTING_ROUTE = HelpJobScreens.SETTING_SCREEN
    const val LANGUAGE_SETTING_ROUTE = HelpJobScreens.LANGUAGE_SETTING_SCREEN
    const val PRIVACY_POLICY_ROUTE = HelpJobScreens.PRIVACY_POLICY_SCREEN
    const val TERMS_OF_SERVICE_ROUTE = HelpJobScreens.TERMS_OF_SERVICE_SCREEN
}

/**
 * 하단바 탭 정의 (경로 + 메타데이터 통합 관리)
 * 하단바 관련 문제는 여기서만 확인하면 됨
 */
enum class BottomNavDestination(
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
    val iconTextId: StringResource,
    val route: String,
) {
    HOME(
        selectedIcon = Res.drawable.home_selected,
        unselectedIcon = Res.drawable.home_unselected,
        iconTextId = Res.string.bottom_nav_home,
        route = "home",
    ),
    CALCULATE(
        selectedIcon = Res.drawable.calculate_selected,
        unselectedIcon = Res.drawable.calculate_unselected,
        iconTextId = Res.string.bottom_nav_calculate,
        route = "calculate",
    ),
    CONTENT(
        selectedIcon = Res.drawable.content_selected,
        unselectedIcon = Res.drawable.content_unselected,
        iconTextId = Res.string.bottom_nav_content,
        route = "content",
    ),
    PROFILE(
        selectedIcon = Res.drawable.profile_selected,
        unselectedIcon = Res.drawable.profile_unselected,
        iconTextId = Res.string.bottom_nav_profile,
        route = "profile",
    );

    companion object {
        /**
         * 하단바 탭 경로인지 확인
         */
        fun isBottomTabRoute(route: String?): Boolean {
            return entries.any { it.route == route }
        }
    }
}

/**
 * 모든 네비게이션 동작 관리
 */
class HelpJobNavigationActions(private val navController: NavHostController) {

    // 인증 플로우 - 이전 화면 제거하며 진행
    fun navigateToSignIn() {
        navController.navigate(HelpJobDestinations.SIGN_IN_ROUTE) {
            // 🔥 SPLASH에서 호출되므로, SPLASH를 백스택에서 제거
            popUpTo(HelpJobDestinations.SPLASH_ROUTE) { inclusive = true }
            launchSingleTop = true
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
            // 🔥 회원가입 플로우: [SIGN_IN, NICKNAME_SETUP] → [SIGN_IN, SIGN_UP_SUCCESS]
            // SIGN_IN을 백스택 베이스로 유지, 그 위의 모든 화면(NICKNAME_SETUP) 제거
            // → NICKNAME_SETUP이 백스택에서 제거되므로 뒤로가기로 돌아갈 수 없음
            // → LaunchedEffect 자동 네비게이션 방지
            popUpTo(HelpJobDestinations.SIGN_IN_ROUTE) {
                inclusive = false  // SIGN_IN은 유지
            }
        }
    }

    fun navigateToOnboarding() {
        navController.navigate(HelpJobDestinations.ONBOARDING_ROUTE) {
            popUpTo(HelpJobDestinations.NICKNAME_SETUP_ROUTE) { inclusive = true }
        }
    }

    fun navigateToSettings() {
        navController.navigate(HelpJobDestinations.SETTING_ROUTE)
    }

    fun navigateToLanguageSetting() {
        navController.navigate(HelpJobDestinations.LANGUAGE_SETTING_ROUTE)
    }

    fun navigateToPrivacyPolicy() {
        navController.navigate(HelpJobDestinations.PRIVACY_POLICY_ROUTE)
    }

    fun navigateToTermsOfService() {
        navController.navigate(HelpJobDestinations.TERMS_OF_SERVICE_ROUTE)
    }

    /**
     * 🆕 메인 앱으로 진입 (백스택 전체 제거)
     *
     * 사용 상황:
     * - 로그인 완료 후 메인 앱 진입
     * - 온보딩 완료 후 메인 앱 진입
     *
     * 효과:
     * - SIGN_IN을 포함한 모든 인증 화면을 백스택에서 완전 제거
     * - MAIN_GRAPH가 새로운 백스택의 루트가 됨 (내부적으로 HOME이 startDestination)
     * - 뒤로가기 시 앱 종료
     */
    fun navigateToAppHome() {
        navController.navigate(HelpJobDestinations.MAIN_GRAPH_ROUTE) {
            // 🔥 popUpTo(SIGN_IN) inclusive=true
            // → SIGN_IN을 찾아서, SIGN_IN부터 이동 전 현재 화면까지 모두 제거
            //
            // 예시:
            // - [SIGN_IN] → MAIN: SIGN_IN 제거 → [MAIN_GRAPH]
            // - [SIGN_IN, ONBOARDING] → MAIN: SIGN_IN~ONBOARDING 모두 제거 → [MAIN_GRAPH]
            popUpTo(HelpJobDestinations.SIGN_IN_ROUTE) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    fun navigateToSignInAfterLogout() {
        navController.navigate(HelpJobDestinations.SIGN_IN_ROUTE) {
            // 🔥 MAIN_GRAPH를 백스택에서 완전 제거 (로그아웃 시 이전 화면으로 돌아갈 수 없도록)
            // SPLASH는 navigateToAppHome에서 이미 제거되었으므로
            // MAIN_GRAPH만 타겟으로 지정
            popUpTo(HelpJobDestinations.MAIN_GRAPH_ROUTE) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    /**
     * 하단바 탭 네비게이션 (온보딩, 탭 간 이동용)
     * - 홈을 백스택 베이스로 유지
     * - 어떤 탭에서든 뒤로가기 → 홈으로 이동
     */
    fun navigateToBottomTab(destination: BottomNavDestination) {
        navController.navigate(destination.route) {
            popUpTo(BottomNavDestination.HOME.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToStepDetail() {
        navController.navigate(STEP_DETAIL_ROUTE)
    }
}