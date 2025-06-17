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
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.BottomNavDestination
import unithon.helpjob.HelpJobNavigationActions
import unithon.helpjob.R
import unithon.helpjob.ui.theme.Primary500

@Composable
fun SplashScreen(
    navActions: HelpJobNavigationActions,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigationTarget) {
        when (uiState.navigationTarget) {
            NavigationTarget.Login -> navActions.navigateToSignIn()
            NavigationTarget.Onboarding -> navActions.navigateToOnboarding()
            NavigationTarget.Main -> navActions.navigateToBottomTab(BottomNavDestination.HOME)
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
            painter = painterResource(R.drawable.splash),
            contentDescription = null
        )
    }
}