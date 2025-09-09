package unithon.helpjob.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpJobTopAppBar(
    modifier: Modifier = Modifier,
    @StringRes title: Int? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            title?.let {
                Text(
                    text = stringResource(it),
                    style = MaterialTheme.typography.headlineMedium, // 20sp, Bold
                    color = Grey700
                )
            }
        },
        navigationIcon = {
            onBack?.let {
                IconButton(onClick = it) {
                    Icon(
                        painter = painterResource(id = R.drawable.top_arrowback),
                        contentDescription = stringResource(id = R.string.back_button),
                        tint = Color.Unspecified // 아이콘 자체 색상 사용
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Grey000,
            titleContentColor = Grey700,
            navigationIconContentColor = Grey700
        ),
        windowInsets = WindowInsets(0.dp), // 🆕 시스템바 패딩 제거
        modifier = modifier.fillMaxWidth()
    )
}