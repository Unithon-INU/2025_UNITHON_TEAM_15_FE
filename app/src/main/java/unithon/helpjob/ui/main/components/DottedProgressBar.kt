package unithon.helpjob.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.HelpJobTheme
import unithon.helpjob.ui.theme.Primary200
import unithon.helpjob.ui.theme.Primary400
import unithon.helpjob.ui.theme.Primary600
import unithon.helpjob.ui.theme.Warning

@Composable
fun DottedProgressBar(
    progress: Float, // 0.0 to 1.0
    modifier: Modifier = Modifier,
    height: Dp = 15.dp,
    backgroundColor: Color = Primary200,
    progressColor: Color = Primary400,
    cornerRadius: Dp = 20.dp,
    showTicks: Boolean = false,
    tickColor: Color = Primary600,
    tickInterval: Float = 0.1f, // 10% 간격
    tickRadius: Dp = 3.dp, // 원의 반지름
    showPercentage: Boolean = false,
    percentageBubbleColor: Color = Primary400,
    percentageTextColor: Color = Warning
) {
    val density = LocalDensity.current
    val progressPercentage = (progress * 100).toInt()

    Box(modifier = modifier.height(if (showPercentage && progress > 0) height + 40.dp else height)) {
        // 배경 진행바 (showPercentage가 true면 아래쪽에 위치)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .offset(y = if (showPercentage && progress > 0) 30.dp else 0.dp)
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor)
        ) {
            // 진행된 부분
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .background(progressColor, RoundedCornerShape(cornerRadius))
            )
        }

        // 구분점 (원형 틱) 표시
        if (showTicks) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .offset(y = if (showPercentage && progress > 0) 30.dp else 0.dp)
            ) {
                val barHeight = size.height
                val barWidth = size.width
                val tickRadiusPx = with(density) { tickRadius.toPx() }

                // 10% 간격으로 원형 구분점 그리기 (0%와 100%는 제외)
                for (i in 1..9) {
                    val tickPosition = barWidth * (i * tickInterval)
                    drawCircle(
                        color = tickColor,
                        radius = tickRadiusPx,
                        center = Offset(
                            x = tickPosition,
                            y = barHeight / 2 // 진행바의 세로 정가운데
                        )
                    )
                }
            }
        }

        // 현재 진행률 끝 부분에 퍼센티지 물풍선 표시
        if (showPercentage && progress > 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress) // 진행된 부분만큼만 차지
                    .height(30.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd) // 오른쪽 끝에 배치
                        .offset(x = 20.dp) // 물풍선 너비의 절반만큼 오른쪽으로 이동
                ) {
                    PercentageBubble(
                        percentage = progressPercentage,
                        backgroundColor = percentageBubbleColor,
                        textColor = percentageTextColor
                    )
                }
            }
        }
    }
}

@Composable
private fun PercentageBubble(
    percentage: Int,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // 물풍선 몸체
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
        ) {
            Text(
                text = "${percentage}%",
                style = MaterialTheme.typography.titleSmall.copy(
                ),
                color = textColor,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }

        // 물풍선 꼬리 (삼각형)
        Canvas(
            modifier = Modifier
                .size(6.5.dp, 5.9.dp)
                .offset(y = (-1).dp)
        ) {
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(size.width / 2, size.height) // 아래쪽 중앙에서 시작
                lineTo(0f, 0f) // 왼쪽 위
                lineTo(size.width, 0f) // 오른쪽 위
                close()
            }
            drawPath(
                path = path,
                color = backgroundColor
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DottedProgressBarPreview() {
    HelpJobTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "진행바 예제",
                style = MaterialTheme.typography.headlineMedium,
                color = Grey700
            )

            // 70% 진행 - 물풍선 + 틱
            Text(
                text = "70% 진행 (물풍선 + 틱)",
                style = MaterialTheme.typography.titleMedium,
                color = Grey600
            )
            DottedProgressBar(
                progress = 0.7f,
                modifier = Modifier.fillMaxWidth(),
                showTicks = true,
                showPercentage = true
            )

            // 45% 진행 - 물풍선만
            Text(
                text = "45% 진행 (물풍선만)",
                style = MaterialTheme.typography.titleMedium,
                color = Grey600
            )
            DottedProgressBar(
                progress = 0.45f,
                modifier = Modifier.fillMaxWidth(),
                showPercentage = true
            )

            // 30% 진행 - 틱만
            Text(
                text = "30% 진행 (틱만)",
                style = MaterialTheme.typography.titleMedium,
                color = Grey600
            )
            DottedProgressBar(
                progress = 0.3f,
                modifier = Modifier.fillMaxWidth(),
                showTicks = true
            )

            // 85% 진행 - 모든 기능
            Text(
                text = "85% 진행 (모든 기능)",
                style = MaterialTheme.typography.titleMedium,
                color = Grey600
            )
            DottedProgressBar(
                progress = 0.85f,
                modifier = Modifier.fillMaxWidth(),
                showTicks = true,
                showPercentage = true
            )
        }
    }
}