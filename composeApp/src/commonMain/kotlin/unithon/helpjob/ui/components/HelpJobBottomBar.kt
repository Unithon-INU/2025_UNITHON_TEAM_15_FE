package unithon.helpjob.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.BottomNavDestination
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

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 0.5.dp,
                color = borderColor,
                shape = bottomBarShape
            )
            .background(color = Grey000, shape = bottomBarShape)
            .clip(bottomBarShape)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 24.dp, vertical = 7.dp)
            .selectableGroup(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination == destination.route

            Column(
                modifier = Modifier
                    .semantics {
                        role = Role.Tab
                        this.selected = selected
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false),
                        onClick = { onNavigateToDestination(destination) }
                    )
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painter = painterResource(
                        if (selected) destination.selectedIcon else destination.unselectedIcon
                    ),
                    contentDescription = stringResource(destination.iconTextId),
                    tint = Color.Unspecified
                )
                Text(
                    text = stringResource(destination.iconTextId),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected) Primary600 else Grey300
                )
            }
        }
    }
}
