package unithon.helpjob

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import unithon.helpjob.ui.auth.nickname.NicknameSetupScreen
import unithon.helpjob.ui.auth.signin.SignInScreen
import unithon.helpjob.ui.auth.signup.SignUpScreen
import unithon.helpjob.ui.auth.signup.SignUpSuccessScreen
import unithon.helpjob.ui.calculator.CalculatorScreen
import unithon.helpjob.ui.document.DocumentScreen
import unithon.helpjob.ui.main.HomeScreen
import unithon.helpjob.ui.main.HomeViewModel
import unithon.helpjob.ui.main.TempScreen
import unithon.helpjob.ui.main.page.StepDetailScreen
import unithon.helpjob.ui.onboarding.OnboardingScreen
import unithon.helpjob.ui.splash.SplashScreen

@Composable
fun HelpJobNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navActions: HelpJobNavigationActions = HelpJobNavigationActions(navController),
    startDestination: String = HelpJobDestinations.SPLASH_ROUTE // 로그인 작업 위해
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = HelpJobDestinations.SPLASH_ROUTE) {
            SplashScreen(navActions = navActions)
        }

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
                onNicknameSet = navActions::navigateToSignUpSuccess,
                onBack = { navController.popBackStack() } // 🆕 뒤로가기 추가
            )
        }

        composable(route = HelpJobDestinations.SIGN_UP_SUCCESS_ROUTE) {
            SignUpSuccessScreen(
                onGoToLogin = navActions::navigateToSignIn
            )
        }

        composable(route = HelpJobDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(
                onOnboardingComplete = { navActions.navigateToBottomTab(BottomNavDestination.HOME) }
            )
        }

        // 메인 앱 플로우 (하단바 있음) - enum에서 경로 가져옴
        composable(route = BottomNavDestination.HOME.route) {
            HomeScreen(
                onNavigateToStepDetail = {
                    navActions.navigateToStepDetail()
                }
            )
        }

        composable(route = HelpJobDestinations.STEP_DETAIL_ROUTE) { backStackEntry ->
            // HOME 화면의 ViewModel을 가져와서 공유
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(BottomNavDestination.HOME.route)
            }
            val homeViewModel: HomeViewModel = hiltViewModel(parentEntry)

            StepDetailScreen(
                onBackClick = {
                    homeViewModel.clearSelectedStep()
                    navController.popBackStack()
                },
                viewModel = homeViewModel // 공유된 ViewModel 전달
            )
        }

        composable(route = BottomNavDestination.CALCULATE.route) {
            CalculatorScreen(
            )
        }

        composable(route = BottomNavDestination.CONTENT.route) {
            DocumentScreen(

            )
        }

        composable(route = BottomNavDestination.PROFILE.route) {
            TempScreen(
                onNavigateToSignIn = navActions::navigateToSignIn
            )
        }
    }
}