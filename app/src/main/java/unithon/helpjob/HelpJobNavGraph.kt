package unithon.helpjob

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import unithon.helpjob.ui.auth.nickname.NicknameSetupScreen
import unithon.helpjob.ui.auth.signin.SignInScreen
import unithon.helpjob.ui.auth.signup.SignUpScreen
import unithon.helpjob.ui.main.TempScreen
import unithon.helpjob.ui.onboarding.OnboardingScreen

@Composable
fun HelpJobNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navActions: HelpJobNavigationActions = HelpJobNavigationActions(navController),
    startDestination: String = HelpJobDestinations.SIGN_IN_ROUTE // 로그인 작업 위해
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 인증 플로우 (하단바 없음)
        composable(route = HelpJobDestinations.SIGN_IN_ROUTE) {
            SignInScreen(
                onNavigateToSignUp = navActions::navigateToSignUp,
                onNavigateToOnboarding = { navActions.navigateToOnboarding() }
            )
        }

        composable(route = HelpJobDestinations.SIGN_UP_ROUTE) {
            SignUpScreen(
                onNavigateToNicknameSetup = navActions::navigateToNicknameSetup,
                onBack = { navController.popBackStack() } // 🆕 뒤로가기 추가
            )
        }

        composable(route = HelpJobDestinations.NICKNAME_SETUP_ROUTE) {
            NicknameSetupScreen(
                onNicknameSet = navActions::navigateToOnboarding,
                onBack = { navController.popBackStack() } // 🆕 뒤로가기 추가
            )
        }

        composable(route = HelpJobDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(
                onOnboardingComplete = { navActions.navigateToBottomTab(BottomNavDestination.HOME) }
            )
        }

        // 메인 앱 플로우 (하단바 있음) - enum에서 경로 가져옴
        composable(route = BottomNavDestination.HOME.route) {
            TempScreen(
                onNavigateToSignIn = navActions::navigateToSignIn
            )
        }

        composable(route = BottomNavDestination.CALCULATE.route) {
            TempScreen(
                onNavigateToSignIn = navActions::navigateToSignIn
            )
        }

        composable(route = BottomNavDestination.CONTENT.route) {
            TempScreen(
                onNavigateToSignIn = navActions::navigateToSignIn
            )
        }

        composable(route = BottomNavDestination.PROFILE.route) {
            TempScreen(
                onNavigateToSignIn = navActions::navigateToSignIn
            )
        }
    }
}