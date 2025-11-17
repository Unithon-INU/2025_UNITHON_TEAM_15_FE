package unithon.helpjob.ui.calculator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 급여 계산기 화면 (KMP 공용)
 * Android 구현은 androidMain에 있음
 */
@Composable
expect fun CalculatorScreen(
    viewModel: unithon.helpjob.ui.calculator.CalculatorViewModel,
    onBackClick: () -> Unit
)
