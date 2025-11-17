package unithon.helpjob.ui.document

import androidx.compose.runtime.Composable

/**
 * 서류 생성 화면 (KMP 공용)
 * Android 구현은 androidMain에 있음
 */
@Composable
expect fun DocumentScreen(
    viewModel: unithon.helpjob.ui.document.DocumentViewModel,
    onBackClick: () -> Unit,
    onNavigateToEmailVerification: () -> Unit,
    onNavigateToPersonalInfo: () -> Unit,
    onNavigateToWorkplaceInfo1: () -> Unit,
    onNavigateToWorkplaceInfo2: () -> Unit,
    onNavigateToWorkplaceInfo3: () -> Unit,
    onNavigateToWorkplaceInfo4: () -> Unit,
    onNavigateToWorkingHours: () -> Unit,
    onNavigateToConfirmation: () -> Unit,
    onNavigateToCompletion: () -> Unit,
)
