package unithon.helpjob.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import unithon.helpjob.BottomNavDestination
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Primary600

@Composable
fun HelpJobBottomBar(
    destinations: List<BottomNavDestination>,
    currentDestination: String?,
    onNavigateToDestination: (BottomNavDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Grey000
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination == destination.route

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(destination.iconTextId))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary600,
                    unselectedIconColor = Grey300,
                    selectedTextColor = Primary600,
                    unselectedTextColor = Grey300
                )
            )
        }
    }
}