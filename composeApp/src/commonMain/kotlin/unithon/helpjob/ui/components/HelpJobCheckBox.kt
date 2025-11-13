package unithon.helpjob.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.ic_check
import org.jetbrains.compose.resources.painterResource
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Primary600

@Composable
fun HelpJobCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    cornerRadius: Dp = 4.dp,
    checkedColor: Color = Grey000,
    uncheckedColor: Color = Grey000,
    borderColor: Color = Primary600,
    checkmarkColor: Color = Primary600,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                color = if (checked) checkedColor else uncheckedColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .clickable(enabled = enabled) {
                onCheckedChange(!checked)
            },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                painter = painterResource(Res.drawable.ic_check),
                contentDescription = "체크됨",
                tint = checkmarkColor,
                modifier = Modifier.size(size * 0.6f)
            )
        }
    }
}