package unithon.helpjob

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.sharedKoinViewModel
import unithon.helpjob.ui.auth.nickname.NicknameSetupScreen
import unithon.helpjob.ui.auth.signin.SignInScreen
import unithon.helpjob.ui.auth.signup.SignUpScreen
import unithon.helpjob.ui.auth.signup.SignUpSuccessScreen
import unithon.helpjob.ui.calculator.CalculatorScreen
import unithon.helpjob.ui.document.DocumentScreen
import unithon.helpjob.ui.main.HomeScreen
import unithon.helpjob.ui.main.HomeViewModel
import unithon.helpjob.ui.main.page.StepDetailScreen
import unithon.helpjob.ui.onboarding.OnboardingScreen
import unithon.helpjob.ui.profile.ProfileScreen
import unithon.helpjob.ui.setting.LanguageSettingScreen
import unithon.helpjob.ui.setting.PrivacyPolicyScreen
import unithon.helpjob.ui.setting.SettingScreen
import unithon.helpjob.ui.setting.TermsOfServiceScreen
import unithon.helpjob.ui.splash.SplashScreen

@Composable
fun HelpJobNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navActions: HelpJobNavigationActions = HelpJobNavigationActions(navController),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    startDestination: String = HelpJobDestinations.SPLASH_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = HelpJobDestinations.ROOT_GRAPH_ROUTE,  // 🔥 Graph 레벨 route 추가
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(route = HelpJobDestinations.SPLASH_ROUTE) {
            SplashScreen(navActions = navActions)
        }

        // 인증 플로우 (하단바 없음)
        composable(route = HelpJobDestinations.SIGN_IN_ROUTE) {
            SignInScreen(
                onNavigateToSignUp = navActions::navigateToSignUp,
                onNavigateToOnboarding = navActions::navigateToOnboarding,
                snackbarHostState = snackbarHostState,
                onNavigateToHome = navActions::navigateToAppHome
            )
        }

        composable(route = HelpJobDestinations.SIGN_UP_ROUTE) {
            SignUpScreen(
                onNavigateToNicknameSetup = navActions::navigateToNicknameSetup,
                onBack = { navController.popBackStack() },
                snackbarHostState = snackbarHostState
            )
        }

        composable(route = HelpJobDestinations.NICKNAME_SETUP_ROUTE) {
            NicknameSetupScreen(
                onNicknameSet = navActions::navigateToSignUpSuccess,
                onBack = { navController.popBackStack() },
                snackbarHostState = snackbarHostState,
            )
        }

        composable(route = HelpJobDestinations.SIGN_UP_SUCCESS_ROUTE) {
            SignUpSuccessScreen(
                onGoToLogin = navActions::navigateToSignIn
            )
        }

        composable(route = HelpJobDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(
                onOnboardingComplete = navActions::navigateToAppHome,
                snackbarHostState = snackbarHostState
            )
        }

        // 메인 앱 플로우 (하단바 있음)
        // 🔥 Graph 레벨의 backStackEntry를 사용하여 모든 화면이 동일한 HomeViewModel 공유
        composable(route = BottomNavDestination.HOME.route) {
            val graphEntry = remember(navController) {
                navController.getBackStackEntry(HelpJobDestinations.ROOT_GRAPH_ROUTE)
            }
            val homeViewModel = graphEntry.sharedKoinViewModel<HomeViewModel>(navController)

            HomeScreen(
                onNavigateToStepDetail = { navActions.navigateToStepDetail() },
                snackbarHostState = snackbarHostState,
                viewModel = homeViewModel
            )
        }

        composable(route = HelpJobDestinations.STEP_DETAIL_ROUTE) {
            val graphEntry = remember(navController) {
                navController.getBackStackEntry(HelpJobDestinations.ROOT_GRAPH_ROUTE)
            }
            val homeViewModel = graphEntry.sharedKoinViewModel<HomeViewModel>(navController)

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
            DocumentScreen(
                snackbarHostState = snackbarHostState
            )
        }
        // HOME 화면의 ViewModel을 가져와서 공유 (StepDetailScreen과 동일한 패턴)
        composable(route = BottomNavDestination.PROFILE.route) {
            val graphEntry = remember(navController) {
                navController.getBackStackEntry(HelpJobDestinations.ROOT_GRAPH_ROUTE)
            }
            val homeViewModel = graphEntry.sharedKoinViewModel<HomeViewModel>(navController)

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
                homeViewModel = homeViewModel,
                snackbarHostState = snackbarHostState,
            )
        }

        composable(route = HelpJobDestinations.SETTING_ROUTE) {
            val graphEntry = remember(navController) {
                navController.getBackStackEntry(HelpJobDestinations.ROOT_GRAPH_ROUTE)
            }
            val homeViewModel = graphEntry.sharedKoinViewModel<HomeViewModel>(navController)

            SettingScreen(
                onBack = { navController.popBackStack() },
                onLanguageSettingClick = navActions::navigateToLanguageSetting,
                onPrivacyPolicyClick = navActions::navigateToPrivacyPolicy,
                onTermsOfServiceClick = navActions::navigateToTermsOfService,
                onLogoutClick = navActions::navigateToSignInAfterLogout,
                snackbarHostState = snackbarHostState,
                modifier = modifier,
                homeViewModel = homeViewModel
            )
        }

        composable(route = HelpJobDestinations.LANGUAGE_SETTING_ROUTE) {
            val graphEntry = remember(navController) {
                navController.getBackStackEntry(HelpJobDestinations.ROOT_GRAPH_ROUTE)
            }
            val homeViewModel = graphEntry.sharedKoinViewModel<HomeViewModel>(navController)

            LanguageSettingScreen(
                onBack = { navController.popBackStack() },
                snackbarHostState = snackbarHostState,
                modifier = modifier,
                homeViewModel = homeViewModel
            )
        }

        composable(route = HelpJobDestinations.PRIVACY_POLICY_ROUTE) {
            PrivacyPolicyScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = HelpJobDestinations.TERMS_OF_SERVICE_ROUTE) {
            TermsOfServiceScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}