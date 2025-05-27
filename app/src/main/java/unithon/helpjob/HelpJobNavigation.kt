package unithon.helpjob

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

/**
 * 인증 플로우 경로만 관리 (명확한 책임 분리)
 */
object HelpJobDestinations {
    const val SIGN_IN_ROUTE = "sign_in"
    const val SIGN_UP_ROUTE = "sign_up"
    const val NICKNAME_SETUP_ROUTE = "nickname_setup"
    const val ONBOARDING_ROUTE = "onboarding"
}

/**
 * 하단바 탭 정의 (경로 + 메타데이터 통합 관리)
 * 하단바 관련 문제는 여기서만 확인하면 됨
 */
enum class BottomNavDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    val route: String,  // 직접 정의, 중복 제거
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.bottom_nav_home,
        route = "home",  // 중복 제거
    ),
    CALCULATE(
        selectedIcon = Icons.Filled.Person, // TODO: 계산기 아이콘
        unselectedIcon = Icons.Outlined.Person,
        iconTextId = R.string.bottom_nav_calculate,
        route = "calculate",
    ),
    CONTENT(
        selectedIcon = Icons.Filled.Person, // TODO: 콘텐츠 아이콘
        unselectedIcon = Icons.Outlined.Person,
        iconTextId = R.string.bottom_nav_content,
        route = "content",
    ),
    PROFILE(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
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

    fun navigateToOnboarding() {
        navController.navigate(HelpJobDestinations.ONBOARDING_ROUTE) {
            popUpTo(HelpJobDestinations.NICKNAME_SETUP_ROUTE) { inclusive = true }
        }
    }

    // 하단바 탭 네비게이션 - 상태 유지하며 전환
    fun navigateToHome() {
        navigateToBottomTab(BottomNavDestination.HOME)
    }

    fun navigateToCalculate() {
        navigateToBottomTab(BottomNavDestination.CALCULATE)
    }

    fun navigateToContent() {
        navigateToBottomTab(BottomNavDestination.CONTENT)
    }

    fun navigateToProfile() {
        navigateToBottomTab(BottomNavDestination.PROFILE)
    }

    /**
     * 하단바 탭 공통 네비게이션 로직
     */
    private fun navigateToBottomTab(destination: BottomNavDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}