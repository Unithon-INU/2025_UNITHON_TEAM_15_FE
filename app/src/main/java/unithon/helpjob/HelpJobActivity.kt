package unithon.helpjob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                Scaffold(
                    bottomBar = {
                        HelpJobBottomBar(
                            destinations = BottomNavDestination.entries,
                            currentDestination = null, // TODO: 나중에 현재 라우트 연결
                            onNavigateToDestination = { destination ->
                                // TODO: 네비게이션 로직 연결
                            }
                        )
                    }
                ) { innerPadding ->
                    HelpJobNavGraph(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}