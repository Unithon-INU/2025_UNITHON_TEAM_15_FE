package unithon.helpjob.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.exclamation_mark
import helpjob.composeapp.generated.resources.step_warning_dialog_cancel
import helpjob.composeapp.generated.resources.step_warning_dialog_continue
import helpjob.composeapp.generated.resources.step_warning_dialog_message
import helpjob.composeapp.generated.resources.step_warning_dialog_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Primary400
import unithon.helpjob.ui.theme.Warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepProgressWarningDialog(
    onDismiss: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 28.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 경고 아이콘
                Spacer(Modifier.height(23.dp))
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(
                            color = Primary400,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.exclamation_mark),
                        contentDescription = null,
                        tint = Warning,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // 제목
                Text(
                    text = stringResource(Res.string.step_warning_dialog_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                    ),
                    color = Grey600,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                // 설명
                Text(
                    text = stringResource(Res.string.step_warning_dialog_message),
                    style = MaterialTheme.typography.labelMedium,
                    color = Grey600,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(25.dp))

                // 버튼들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 취소 버튼
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Grey300,
                            contentColor = Grey500
                        ),
                        contentPadding = PaddingValues(
                            vertical = 12.dp,
                            horizontal = 10.dp
                        )
                    ) {
                        Text(
                            text = stringResource(Res.string.step_warning_dialog_cancel),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight(600),
                                textAlign = TextAlign.Center,
                                color = Grey500
                            )
                        )
                    }

                    Button(
                        onClick = onContinue,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary400,
                            contentColor = Grey600
                        ),
                        contentPadding = PaddingValues(
                            vertical = 12.dp,
                            horizontal = 10.dp
                        )
                    ) {
                        Text(
                            text = stringResource(Res.string.step_warning_dialog_continue),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight(600),
                                textAlign = TextAlign.Center,
                                color = Grey500
                            )
                        )
                    }
                }
                Spacer(Modifier.height(19.dp))
            }
        }
    }
}
