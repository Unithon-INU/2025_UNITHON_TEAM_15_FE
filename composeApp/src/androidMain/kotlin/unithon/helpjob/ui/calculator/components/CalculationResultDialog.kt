package unithon.helpjob.ui.calculator.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import dev.icerock.moko.resources.compose.stringResource
import unithon.helpjob.R
import unithon.helpjob.resources.MR
import unithon.helpjob.ui.theme.Blue500
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey100
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.HelpJobTheme
import unithon.helpjob.ui.theme.Warning
import unithon.helpjob.util.noRippleClickable
import java.text.NumberFormat
import java.util.Locale

data class CalculationResult(
    val workHours: Int,
    val weeklyAllowanceHours: Int,
    val totalAmount: Int,
    val includesWeeklyAllowance: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationResultDialog(
    result: CalculationResult?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    result?.let {
        BasicAlertDialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            ),
            modifier = modifier
        ) {
            Card(
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Grey000
                ),
                modifier = Modifier
                    .padding(horizontal = 39.dp)
                    .fillMaxWidth()
                    .noRippleClickable { onDismiss() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 31.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(37.dp))

                    // 완료 이미지
                    Image(
                        painter = painterResource(R.drawable.signup_finish),
                        contentDescription = null,
                        modifier = Modifier.size(97.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    // 근로시간 (양끝 정렬)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(MR.strings.calculator_result_work_hours_label),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Grey600
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${result.workHours}",
                                style = MaterialTheme.typography.headlineMedium.copy(lineHeight = 24.sp),
                                color = Grey700
                            )
                            Text(
                                text = " ${stringResource(MR.strings.calculator_result_hours_unit)}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Grey600
                            )
                        }
                    }

                    Spacer(Modifier.height(13.dp))

                    // 주휴수당 (조건부 로직)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(MR.strings.calculator_result_weekly_allowance_label),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Grey600
                        )
                        if (result.includesWeeklyAllowance) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = NumberFormat.getNumberInstance(Locale.KOREA).format(result.weeklyAllowanceHours),
                                    style = MaterialTheme.typography.headlineMedium.copy(lineHeight = 24.sp),
                                    color = Grey700
                                )
                                Text(
                                    text = " ${stringResource(MR.strings.calculator_result_included)}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Grey600
                                )
                            }
                        } else {
                            Text(
                                text = stringResource(MR.strings.calculator_result_not_included),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Grey600
                            )
                        }
                    }

                    Spacer(Modifier.height(13.dp))

                    // 예상 월급 (양끝 정렬)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(MR.strings.calculator_result_expected_salary_label),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Grey600
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = NumberFormat.getNumberInstance(Locale.KOREA).format(result.totalAmount),
                                style = MaterialTheme.typography.headlineMedium.copy(lineHeight = 24.sp),
                                color = Grey700
                            )
                            Text(
                                text = " ${stringResource(MR.strings.calculator_result_won_unit)}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Grey600
                            )
                        }
                    }

                    Spacer(Modifier.height(13.dp))

                    // 주휴수당 조건 설명 (아이콘 + 텍스트)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (result.includesWeeklyAllowance) {
                            Icon(
                                painter = painterResource(R.drawable.calculate_check),
                                contentDescription = null,
                                tint = Blue500,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(5.8.dp))
                            Text(
                                text = stringResource(MR.strings.calculator_result_weekly_allowance_condition_met),
                                style = MaterialTheme.typography.labelMedium,
                                color = Blue500
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.calculate_exclamation),
                                contentDescription = null,
                                tint = Warning,
                                modifier = Modifier.height(15.dp)
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = stringResource(MR.strings.calculator_result_weekly_allowance_condition_not_met),
                                style = MaterialTheme.typography.labelMedium,
                                color = Warning
                            )
                        }
                    }

                    Spacer(Modifier.height(38.dp))

                    // 면책 조항 (Grey100 박스)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Grey100,
                                shape = RoundedCornerShape(size = 5.dp)
                            )
                            .padding(start = 25.dp, top = 15.dp, end = 25.dp, bottom = 15.dp)
                    ) {
                        Text(
                            text = stringResource(MR.strings.calculator_result_disclaimer),
                            style = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center),
                            color = Grey500,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(22.dp))
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    locale = "ko"
)
@Composable
fun CalculationResultDialogWithWeeklyAllowancePreview() {
    HelpJobTheme {
        CalculationResultDialog(
            result = CalculationResult(
                workHours = 15,
                weeklyAllowanceHours = 100300,
                totalAmount = 501500,
                includesWeeklyAllowance = true
            ),
            onDismiss = {}
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    locale = "ko"
)
@Composable
fun CalculationResultDialogWithoutWeeklyAllowancePreview() {
    HelpJobTheme {
        CalculationResultDialog(
            result = CalculationResult(
                workHours = 10,
                weeklyAllowanceHours = 0,
                totalAmount = 401200,
                includesWeeklyAllowance = false
            ),
            onDismiss = {}
        )
    }
}