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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.calculate_check
import helpjob.composeapp.generated.resources.calculate_exclamation
import helpjob.composeapp.generated.resources.calculator_result_disclaimer
import helpjob.composeapp.generated.resources.calculator_result_expected_salary_label
import helpjob.composeapp.generated.resources.calculator_result_hours_unit
import helpjob.composeapp.generated.resources.calculator_result_included
import helpjob.composeapp.generated.resources.calculator_result_not_included
import helpjob.composeapp.generated.resources.calculator_result_weekly_allowance_condition_met
import helpjob.composeapp.generated.resources.calculator_result_weekly_allowance_condition_not_met
import helpjob.composeapp.generated.resources.calculator_result_weekly_allowance_excluded_by_user
import helpjob.composeapp.generated.resources.calculator_result_weekly_allowance_label
import helpjob.composeapp.generated.resources.calculator_result_won_unit
import helpjob.composeapp.generated.resources.calculator_result_work_hours_label
import helpjob.composeapp.generated.resources.signup_finish
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.ui.theme.Blue500
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey100
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.Warning
import unithon.helpjob.util.NumberFormatter
import unithon.helpjob.util.noRippleClickable

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
                        painter = painterResource(Res.drawable.signup_finish),
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
                            text = stringResource(Res.string.calculator_result_work_hours_label),
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
                                text = " ${stringResource(Res.string.calculator_result_hours_unit)}",
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
                            text = stringResource(Res.string.calculator_result_weekly_allowance_label),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Grey600
                        )
                        if (result.includesWeeklyAllowance) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = NumberFormatter.formatNumber(result.weeklyAllowanceHours),
                                    style = MaterialTheme.typography.headlineMedium.copy(lineHeight = 24.sp),
                                    color = Grey700
                                )
                                Text(
                                    text = " ${stringResource(Res.string.calculator_result_included)}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Grey600
                                )
                            }
                        } else {
                            Text(
                                text = stringResource(Res.string.calculator_result_not_included),
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
                            text = stringResource(Res.string.calculator_result_expected_salary_label),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Grey600
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = NumberFormatter.formatNumber(result.totalAmount),
                                style = MaterialTheme.typography.headlineMedium.copy(lineHeight = 24.sp),
                                color = Grey700
                            )
                            Text(
                                text = " ${stringResource(Res.string.calculator_result_won_unit)}",
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
                            // 케이스 1: 15시간 이상 + 포함 선택
                            Icon(
                                painter = painterResource(Res.drawable.calculate_check),
                                contentDescription = null,
                                tint = Blue500,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(5.8.dp))
                            Text(
                                text = stringResource(Res.string.calculator_result_weekly_allowance_condition_met),
                                style = MaterialTheme.typography.labelMedium,
                                color = Blue500
                            )
                        } else if (result.weeklyAllowanceExcludedByUser) {
                            // 케이스 2: 15시간 이상이지만 미포함 선택
                            Icon(
                                painter = painterResource(Res.drawable.calculate_exclamation),
                                contentDescription = null,
                                tint = Warning,
                                modifier = Modifier.height(15.dp)
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = stringResource(Res.string.calculator_result_weekly_allowance_excluded_by_user),
                                style = MaterialTheme.typography.labelMedium,
                                color = Warning
                            )
                        } else {
                            // 케이스 3: 15시간 미달
                            Icon(
                                painter = painterResource(Res.drawable.calculate_exclamation),
                                contentDescription = null,
                                tint = Warning,
                                modifier = Modifier.height(15.dp)
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = stringResource(Res.string.calculator_result_weekly_allowance_condition_not_met),
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
                            text = stringResource(Res.string.calculator_result_disclaimer),
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
