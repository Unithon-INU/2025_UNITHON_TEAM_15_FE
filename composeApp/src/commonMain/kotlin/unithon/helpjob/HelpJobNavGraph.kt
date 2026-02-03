package unithon.helpjob

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
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
import unithon.helpjob.ui.profile.ProfileViewModel
import unithon.helpjob.ui.profile.edit.ProfileEditScreen
import unithon.helpjob.ui.profile.edit.ProfileEditViewModel
import unithon.helpjob.data.model.ProfileField
import unithon.helpjob.ui.setting.LanguageSettingScreen
import unithon.helpjob.ui.setting.PrivacyPolicyScreen
import unithon.helpjob.ui.setting.SettingScreen
import unithon.helpjob.ui.setting.TermsOfServiceScreen
import unithon.helpjob.ui.setting.WithdrawalScreen
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
        route = HelpJobDestinations.ROOT_GRAPH_ROUTE,
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
                onNavigateToHome = navActions::navigateToAppHome,
                onContinueAsGuest = navActions::navigateToOnboarding  // 🆕 Guest Mode: 온보딩으로 이동
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
                onGoToLogin = {
                    // 🔥 SignUpSuccess → SignIn: SignUpSuccess를 백스택에서 제거
                    navController.navigate(HelpJobDestinations.SIGN_IN_ROUTE) {
                        popUpTo(HelpJobDestinations.SIGN_UP_SUCCESS_ROUTE) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = HelpJobDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(
                onOnboardingComplete = navActions::navigateToAppHome,
                snackbarHostState = snackbarHostState,
                onNavigateToTermsOfService = navActions::navigateToTermsOfService,
                onNavigateToPrivacyPolicy = navActions::navigateToPrivacyPolicy
            )
        }

        // 메인 앱 플로우 (하단바 있음) - HomeViewModel 공유 영역
        navigation(
            route = HelpJobDestinations.MAIN_GRAPH_ROUTE,
            startDestination = BottomNavDestination.HOME.route
        ) {
            composable(route = BottomNavDestination.HOME.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(HelpJobDestinations.MAIN_GRAPH_ROUTE)
                }
                val homeViewModel = koinViewModel<HomeViewModel>(
                    viewModelStoreOwner = parentEntry
                )

                HomeScreen(
                    onNavigateToStepDetail = { navActions.navigateToStepDetail() },
                    snackbarHostState = snackbarHostState,
                    viewModel = homeViewModel
                )
            }

            composable(route = HelpJobDestinations.STEP_DETAIL_ROUTE) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(HelpJobDestinations.MAIN_GRAPH_ROUTE)
                }
                val homeViewModel = koinViewModel<HomeViewModel>(
                    viewModelStoreOwner = parentEntry
                )

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
                    onNavigateToSignIn = navActions::navigateToSignInAfterLogout,  // 🆕 Guest → Member 전환
                    snackbarHostState = snackbarHostState
                )
            }

            composable(route = BottomNavDestination.PROFILE.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(HelpJobDestinations.MAIN_GRAPH_ROUTE)
                }
                val homeViewModel = koinViewModel<HomeViewModel>(
                    viewModelStoreOwner = parentEntry
                )
                val profileViewModel = koinViewModel<ProfileViewModel>(
                    viewModelStoreOwner = parentEntry
                )

                ProfileScreen(
                    onNavigateToSettings = navActions::navigateToSettings,
                    onNavigateToProfileEdit = { profileField, _ ->
                        navActions.navigateToProfileEdit(profileField.name)
                    },
                    onNavigateToHomeWithStep = { stepId ->
                        val targetStep = homeViewModel.homeState.value.steps.find { it.checkStep == stepId }
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
                    onNavigateToSignIn = navActions::navigateToSignInAfterLogout,  // 🆕 Guest → Member 전환
                    homeViewModel = homeViewModel,
                    snackbarHostState = snackbarHostState,
                    viewModel = profileViewModel
                )
            }

            composable(
                route = "${HelpJobDestinations.PROFILE_EDIT_ROUTE}/{field}",
                arguments = listOf(navArgument("field") { type = NavType.StringType })
            ) { backStackEntry ->
                val fieldName = backStackEntry.arguments?.getString("field") ?: return@composable
                val profileField = ProfileField.valueOf(fieldName)

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(HelpJobDestinations.MAIN_GRAPH_ROUTE)
                }
                val profileViewModel = koinViewModel<ProfileViewModel>(
                    viewModelStoreOwner = parentEntry
                )

                val currentValue = when (profileField) {
                    ProfileField.VISA_TYPE -> profileViewModel.uiState.value.visaType ?: ""
                    ProfileField.LANGUAGE_LEVEL -> profileViewModel.uiState.value.languageLevel ?: ""
                    ProfileField.INDUSTRY -> profileViewModel.uiState.value.industry ?: ""
                }

                val editViewModel = koinViewModel<ProfileEditViewModel>(
                    key = fieldName
                ) { parametersOf(profileField, currentValue) }

                ProfileEditScreen(
                    viewModel = editViewModel,
                    snackbarHostState = snackbarHostState,
                    onBack = { navController.popBackStack() },
                    onSaveSuccess = {
                        profileViewModel.refreshProfile()
                        navController.popBackStack()
                    }
                )
            }

            composable(route = HelpJobDestinations.SETTING_ROUTE) {
                SettingScreen(
                    onBack = { navController.popBackStack() },
                    onLanguageSettingClick = navActions::navigateToLanguageSetting,
                    onPrivacyPolicyClick = navActions::navigateToPrivacyPolicy,
                    onTermsOfServiceClick = navActions::navigateToTermsOfService,
                    onWithdrawalClick = navActions::navigateToWithdrawal,
                    onLogoutClick = navActions::navigateToSignInAfterLogout,
                    snackbarHostState = snackbarHostState,
                    modifier = modifier
                )
            }

            composable(route = HelpJobDestinations.LANGUAGE_SETTING_ROUTE) {
                LanguageSettingScreen(
                    onBack = { navController.popBackStack() },
                    snackbarHostState = snackbarHostState,
                    modifier = modifier
                )
            }
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

        composable(route = HelpJobDestinations.WITHDRAWAL_ROUTE) {
            WithdrawalScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}