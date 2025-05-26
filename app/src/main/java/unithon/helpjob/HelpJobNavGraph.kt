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
import unithon.helpjob.ui.calculator.CalculatorScreen
import unithon.helpjob.ui.main.TempScreen
import unithon.helpjob.ui.onboarding.OnboardingScreen

@Composable
fun HelpJobNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = HelpJobDestinations.CALCULATOR
) {
    val navActions = HelpJobNavigationActions(navController)

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 로그인 화면
        composable(route = HelpJobDestinations.SIGN_IN_ROUTE) {
            SignInScreen(
                onNavigateToSignUp = {
                    navActions.navigateToSignUp()
                },
                onNavigateToMain = {
                    navActions.navigateToMain()
                }
            )
        }

        // 회원가입 화면
        composable(route = HelpJobDestinations.SIGN_UP_ROUTE) {
            SignUpScreen(
                onNavigateToNicknameSetup = {
                    navActions.navigateToNicknameSetup()
                }
            )
        }

        // 닉네임 설정 화면
        composable(route = HelpJobDestinations.NICKNAME_SETUP_ROUTE) {
            NicknameSetupScreen(
                onNicknameSet = {
                    navActions.navigateToMain()
                }
            )
        }

        // 임시 메인 화면
        composable(route = HelpJobDestinations.MAIN_ROUTE) {
            TempScreen(
                onNavigateToSignIn = {
                    navActions.navigateToSignIn()
                }
            )
        }

        // TODO: Onboarding 화면 추가
        composable(route = HelpJobDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(
                onOnboardingComplete = {
                    navActions.navigateToMain()
                }
            )
        }

        composable(route = HelpJobDestinations.CALCULATOR) {
            CalculatorScreen(
            )
        }
    }
}