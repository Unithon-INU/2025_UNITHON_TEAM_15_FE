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
    navActions: HelpJobNavigationActions = HelpJobNavigationActions(navController), // 기본값 제공
    startDestination: String = HelpJobDestinations.SIGN_IN_ROUTE // 로그인부터 시작
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 인증 관련 화면들 (하단바 없음)
        composable(route = HelpJobDestinations.SIGN_IN_ROUTE) {
            SignInScreen(
                onNavigateToSignUp = navActions::navigateToSignUp,
                onNavigateToMain = navActions::navigateToHome // 일관성 있게 NavigationActions 사용
            )
        }

        composable(route = HelpJobDestinations.SIGN_UP_ROUTE) {
            SignUpScreen(
                onNavigateToNicknameSetup = navActions::navigateToNicknameSetup
            )
        }

        composable(route = HelpJobDestinations.NICKNAME_SETUP_ROUTE) {
            NicknameSetupScreen(
                onNicknameSet = navActions::navigateToOnboarding
            )
        }

        composable(route = HelpJobDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(
                onOnboardingComplete = navActions::navigateToHome // 일관성 있게 NavigationActions 사용
            )
        }

        // 메인 앱 화면들 (하단바 있음)
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