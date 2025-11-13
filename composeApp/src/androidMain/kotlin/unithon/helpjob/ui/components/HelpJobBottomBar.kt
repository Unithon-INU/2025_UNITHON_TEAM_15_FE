package unithon.helpjob.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.BottomNavDestination
import unithon.helpjob.data.repository.LanguageAwareScreen
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Primary600
import unithon.helpjob.ui.theme.borderColor

@Composable
fun HelpJobBottomBar(
    destinations: List<BottomNavDestination>,
    currentDestination: String?,
    onNavigateToDestination: (BottomNavDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomBarShape = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp
    )

    LanguageAwareScreen {
        NavigationBar(
            modifier = modifier
                .border(
                    width = 0.5.dp,
                    color = borderColor,
                    shape = bottomBarShape
                )
                .background(color = Grey000, shape = bottomBarShape)
                .clip(bottomBarShape),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            destinations.forEach { destination ->
                val selected = currentDestination == destination.route

                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigateToDestination(destination) },
                    icon = {
                        Icon(
                            painter = painterResource(
                                if (selected) destination.selectedIcon else destination.unselectedIcon
                            ),
                            contentDescription = stringResource(destination.iconTextId),
                            tint = Color.Unspecified // 아이콘 자체 색상 사용
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(destination.iconTextId),
                            style = MaterialTheme.typography.labelMedium // Body4 스타일 사용
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Unspecified, // 아이콘 자체 색상 사용
                        unselectedIconColor = Color.Unspecified, // 아이콘 자체 색상 사용
                        selectedTextColor = Primary600,
                        unselectedTextColor = Grey300,
                        indicatorColor = Color.Transparent // 배경 인디케이터 제거
                    )
                )
            }
        }
    }
}