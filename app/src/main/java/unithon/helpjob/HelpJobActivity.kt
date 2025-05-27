package unithon.helpjob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import unithon.helpjob.ui.components.HelpJobBottomBar
import unithon.helpjob.ui.theme.HelpJobTheme

@AndroidEntryPoint
class HelpJobActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelpJobTheme {
                HelpJobApp()
            }
        }
    }
}

@Composable
fun HelpJobApp() {
    val navController = rememberNavController()
    val navActions = remember(navController) { HelpJobNavigationActions(navController) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 하단 탭 라우트들
    val bottomTabRoutes = listOf("home", "calculate", "content", "profile")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // 하단 탭 화면에서만 하단바 표시
            if (currentRoute in bottomTabRoutes) {
                HelpJobBottomBar(
                    destinations = BottomNavDestination.entries,
                    currentDestination = currentRoute,
                    onNavigateToDestination = { destination ->
                        when (destination) {
                            BottomNavDestination.HOME -> navActions.navigateToHome()
                            BottomNavDestination.CALCULATE -> navActions.navigateToCalculate()
                            BottomNavDestination.CONTENT -> navActions.navigateToContent()
                            BottomNavDestination.PROFILE -> navActions.navigateToProfile()
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        HelpJobNavGraph(
            navController = navController,
            navActions = navActions, // navActions 전달
            modifier = Modifier.padding(innerPadding)
        )
    }
}