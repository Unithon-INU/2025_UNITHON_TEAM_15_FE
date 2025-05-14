package unithon.helpjob.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

// 커스텀 타이포그래피 확장 속성들
val Typography.headline1: androidx.compose.ui.text.TextStyle
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography.headlineLarge

val Typography.headline2: androidx.compose.ui.text.TextStyle
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography.headlineMedium

val Typography.subhead1: androidx.compose.ui.text.TextStyle
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography.titleLarge

val Typography.title1: androidx.compose.ui.text.TextStyle
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography.titleMedium

val Typography.title2: androidx.compose.ui.text.TextStyle
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography.titleSmall

val Typography.body1: androidx.compose.ui.text.TextStyle
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography.bodyLarge

val Typography.body2: androidx.compose.ui.text.TextStyle
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography.bodyMedium

val Typography.body3: androidx.compose.ui.text.TextStyle
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography.bodySmall