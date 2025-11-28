package unithon.helpjob.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.splash
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.HelpJobNavigationActions
import unithon.helpjob.ui.theme.Primary500

@Composable
fun SplashScreen(
    navActions: HelpJobNavigationActions,
    viewModel: SplashViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigationTarget) {
        when (uiState.navigationTarget) {
            NavigationTarget.Login -> navActions.navigateToSignIn()
            NavigationTarget.Onboarding -> navActions.navigateToOnboarding()
            NavigationTarget.Main -> navActions.navigateToAppHome()
            null -> {} // 로딩 중
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary500),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.splash),
            contentDescription = null
        )
    }
}