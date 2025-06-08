package unithon.helpjob.ui.onboarding.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import unithon.helpjob.ui.theme.Grey100
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Primary300

@Composable
fun OnboardingButton(
    modifier: Modifier = Modifier,
    icon: Int? = null,
    mainTitle: String,
    subTitle: String? = null,
    mainTitleFontStyle: TextStyle = MaterialTheme.typography.titleMedium,
    subTitleFontStyle: TextStyle = MaterialTheme.typography.labelMedium,
    disableBackgroundColor: Color = Grey100,
    disableColor: Color = Grey500,
    ableBackgroundColor: Color = Primary300,
    ableColor: Color = Grey600,
    iconMainSpacer: Dp = 10.dp,
    mainSubSpacer: Dp = 1.dp,
    contentPosition: Arrangement.Horizontal = Arrangement.Center,
    shape: Shape = RoundedCornerShape(8.dp),
    onClick: () -> Unit,
    enabled: Boolean = false
){
    Card(
        modifier = modifier
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) ableBackgroundColor else disableBackgroundColor,
            contentColor = if (enabled) ableColor else disableColor,
        ),
        shape = shape
    ) {
        if (subTitle == null){
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = contentPosition,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null){
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = "체크아이콘"
                    )
                    Spacer(modifier = Modifier.width(iconMainSpacer))
                }
                Text(
                    text = mainTitle,
                    style = mainTitleFontStyle,
                )
            }
        } else {
            Row (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = contentPosition,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = mainTitle,
                        style = mainTitleFontStyle,
                    )
                    Spacer(modifier = Modifier.height(mainSubSpacer))
                    Text(
                        text = subTitle,
                        style = subTitleFontStyle,
                    )
                }
            }
        }
    }

}