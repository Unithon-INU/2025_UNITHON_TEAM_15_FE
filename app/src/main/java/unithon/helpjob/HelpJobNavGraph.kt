package unithon.helpjob

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import unithon.helpjob.ui.auth.nickname.NicknameSetupScreen
import unithon.helpjob.ui.auth.signin.SignInScreen
import unithon.helpjob.ui.auth.signup.SignUpScreen
import unithon.helpjob.ui.auth.signup.SignUpSuccessScreen
import unithon.helpjob.ui.calculator.CalculatorScreen
import unithon.helpjob.ui.document.DocumentScreen
import unithon.helpjob.ui.main.HomeScreen
import unithon.helpjob.ui.main.HomeViewModel
import unithon.helpjob.ui.profile.ProfileScreen
import unithon.helpjob.ui.main.page.StepDetailScreen
import unithon.helpjob.ui.onboarding.OnboardingScreen
import unithon.helpjob.ui.setting.SettingScreen
import unithon.helpjob.ui.splash.SplashScreen

@Composable
fun HelpJobNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navActions: HelpJobNavigationActions = HelpJobNavigationActions(navController),
    startDestination: String = HelpJobDestinations.SPLASH_ROUTE
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
                onNavigateToOnboarding = navActions::navigateToOnboarding,
                onNavigateToHome = navActions::navigateToAppHome
            )
        }

        composable(route = HelpJobDestinations.SIGN_UP_ROUTE) {
            SignUpScreen(
                onNavigateToNicknameSetup = navActions::navigateToNicknameSetup,
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = HelpJobDestinations.NICKNAME_SETUP_ROUTE) {
            NicknameSetupScreen(
                onNicknameSet = navActions::navigateToSignUpSuccess,
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = HelpJobDestinations.SIGN_UP_SUCCESS_ROUTE) {
            SignUpSuccessScreen(
                onGoToLogin = navActions::navigateToSignIn
            )
        }

        composable(route = HelpJobDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(
                onOnboardingComplete = navActions::navigateToAppHome
            )
        }

        // 메인 앱 플로우 (하단바 있음)
        composable(route = BottomNavDestination.HOME.route) {
            HomeScreen(
                onNavigateToStepDetail = {
                    navActions.navigateToStepDetail()
                }
            )
        }

        composable(route = HelpJobDestinations.STEP_DETAIL_ROUTE) { backStackEntry ->
            // HOME 화면의 ViewModel을 가져와서 공유 (기존과 동일)
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(BottomNavDestination.HOME.route)
            }
            val homeViewModel: HomeViewModel = hiltViewModel(parentEntry)

            StepDetailScreen(
                onBackClick = {
                    homeViewModel.clearSelectedStep()
                    navController.popBackStack()
                },
                viewModel = homeViewModel
            )
        }

        composable(route = BottomNavDestination.CALCULATE.route) {
            CalculatorScreen()
        }

        composable(route = BottomNavDestination.CONTENT.route) {
            DocumentScreen()
        }

        composable(route = BottomNavDestination.PROFILE.route) { backStackEntry ->
            // HOME 화면의 ViewModel을 가져와서 공유 (StepDetailScreen과 동일한 패턴)
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(BottomNavDestination.HOME.route)
            }
            val homeViewModel: HomeViewModel = hiltViewModel(parentEntry)

            ProfileScreen(
                onNavigateToSettings = navActions::navigateToSettings,
                onNavigateToHomeWithStep = { stepId ->
                    val targetStep = homeViewModel.uiState.value.steps.find { it.checkStep == stepId }
                    targetStep?.let { step ->
                        homeViewModel.selectStep(step)
                    }
                    navController.navigate(BottomNavDestination.HOME.route) {
                        launchSingleTop = true
                        restoreState = false
                        popUpTo(BottomNavDestination.PROFILE.route) {
                            inclusive = true
                        }
                    }
                },
                homeViewModel = homeViewModel
            )
        }

        composable(route = HelpJobDestinations.SETTING_ROUTE) {
            SettingScreen(
                onBack = { navController.popBackStack() },
                onLanguageSettingClick = {
                    // TODO: 언어 설정 화면 구현 시 연결
                },
                onResetProgressClick = {
                    // TODO: 다이얼로그 표시 로직 구현 시 연결
                },
                onLogoutClick = navActions::navigateToSignInAfterLogout,
            )
        }
    }
}