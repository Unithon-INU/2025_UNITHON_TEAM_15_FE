package unithon.helpjob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
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
        // 상태바 아이콘을 항상 검정색으로 고정
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true  // 검정 아이콘 (라이트 모드)
        }
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
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            // 하단 탭 화면에서만 하단바 표시 - enum의 유틸리티 함수 사용
            if (BottomNavDestination.isBottomTabRoute(currentRoute)) {
                HelpJobBottomBar(
                    destinations = BottomNavDestination.entries,
                    currentDestination = currentRoute,
                    onNavigateToDestination = navActions::navigateToBottomTab
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        HelpJobNavGraph(
            navController = navController,
            navActions = navActions,
            snackbarHostState = snackbarHostState,
            modifier = Modifier
                .padding(innerPadding)
                // ⭐ nowinandroid의 핵심: consumeWindowInsets 추가
                .consumeWindowInsets(innerPadding)
        )
    }
}

/**
 * Activity 단에서 각 스크린에 패딩값을 위임했으므로 이곳에 각 스크린의 패딩 규칙을 설명
 * SystemBars 패딩 규칙:
 * - 모든 Screen: statusBarsPadding() 필수
 * - 하단바 없는 Screen: navigationBarsPadding() 추가
 * - 하단바 있는 Screen: navigationBarsPadding() 제외
 */