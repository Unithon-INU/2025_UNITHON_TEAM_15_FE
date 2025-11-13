package unithon.helpjob.ui.main.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.dot
import helpjob.composeapp.generated.resources.exclamation_mark
import helpjob.composeapp.generated.resources.filled_checked
import helpjob.composeapp.generated.resources.ic_check
import helpjob.composeapp.generated.resources.step_detail_screen_subtitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.data.model.response.EmploymentCheckRes
import unithon.helpjob.data.model.response.TipResponseItem
import unithon.helpjob.data.repository.LanguageAwareScreen
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.main.HomeViewModel
import unithon.helpjob.ui.theme.Grey100
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey400
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Primary200
import unithon.helpjob.ui.theme.Primary400
import unithon.helpjob.ui.theme.Primary500
import unithon.helpjob.ui.theme.Warning
import unithon.helpjob.util.noRippleClickable

@Composable
fun StepDetailScreen(
    onBackClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    val selectedStep = uiState.selectedStep
    val tips = uiState.tips

    // 🔍 디버깅 로그
    timber.log.Timber.d("🔍 StepDetailScreen - ViewModel 인스턴스: ${viewModel.hashCode()}")
    timber.log.Timber.d("🔍 StepDetailScreen - selectedStep: ${selectedStep?.checkStep}, steps.size: ${homeState.steps.size}")

    when {
        // 1. 데이터 로딩 중 (steps가 비어있음)
        homeState.steps.isEmpty() -> {
            timber.log.Timber.d("❌ StepDetailScreen - steps가 비어있음")
            LoadingScreen(onBackClick = onBackClick)
        }

        // 2. selectedStep이 null인 경우 (잘못된 접근)
        selectedStep == null -> {
            timber.log.Timber.d("❌ StepDetailScreen - selectedStep이 null")
            ErrorScreen(
                message = "요청하신 단계를 찾을 수 없습니다.",
                onBackClick = onBackClick
            )
        }

        // 3. 정상적인 경우
        else -> {
            StepDetailContent(
                step = selectedStep,
                tips = tips,
                onBackClick = onBackClick
            )
        }
    }
}

@Composable
private fun StepDetailContent(
    step: EmploymentCheckRes,
    tips: List<TipResponseItem>,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    LanguageAwareScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            HelpJobTopAppBar(
                onBack = onBackClick
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(Modifier.height(16.dp))

                // Step 카드
                StepDetailCard(step = step)

                Spacer(Modifier.height(24.dp))

                // Tips 섹션
                if (tips.isNotEmpty()) {
                    TipsSection(tips = tips)
                } else {
                    EmptyTipsSection()
                }

                Spacer(Modifier.height(100.dp))
            }
        }
    }
}


@Composable
private fun LoadingScreen(
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        HelpJobTopAppBar(
            onBack = onBackClick
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Primary500
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "정보를 불러오는 중...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Grey400
                )
            }
        }
    }
}

@Composable
private fun ErrorScreen(
    onBackClick: () -> Unit,
    message: String
) {
    Column(modifier = Modifier.fillMaxSize()) {
        HelpJobTopAppBar(
            onBack = onBackClick
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(40.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.exclamation_mark),
                    contentDescription = "에러",
                    tint = Warning
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Grey600,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                HelpJobButton(
                    text = "이전으로 돌아가기",
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun TipsSection(
    tips: List<TipResponseItem>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.filled_checked),
            contentDescription = "체크",
            tint = Color.Unspecified,
            modifier = Modifier.padding(end = 1.dp)
        )
        Text(
            text = stringResource(Res.string.step_detail_screen_subtitle),
            style = MaterialTheme.typography.headlineMedium,
            color = Grey600
        )
    }

    Spacer(Modifier.height(16.dp))

    tips.forEachIndexed { index, tip ->
        ExpandableTipItem(
            number = index + 1,
            tip = tip
        )
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun EmptyTipsSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_check),
            contentDescription = "체크",
            tint = Primary500,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "이런 것들을 알고가면 좋아요",
            style = MaterialTheme.typography.titleMedium,
            color = Grey600
        )
    }

    Spacer(Modifier.height(20.dp))

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_check),
                contentDescription = "정보 없음",
                tint = Grey400
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "현재 단계에서는\n추가 정보가 준비되지 않았어요",
                style = MaterialTheme.typography.bodyMedium,
                color = Grey400,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun StepDetailCard(
    step: EmploymentCheckRes
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary400
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 11.dp
                )
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Primary200
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    modifier = Modifier.padding(
                        vertical = 7.dp,
                        horizontal = 17.dp
                    ),
                    text = step.checkStep,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Grey600
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = step.stepInfoRes.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Grey600
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = step.stepInfoRes.subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = Grey600
            )
        }
    }
}

@Composable
fun ExpandableTipItem(
    number: Int,
    tip: TipResponseItem
) {
    var isExpanded by remember { mutableStateOf(false) }
    Column {
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Grey200
            ),
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable {
                    isExpanded = !isExpanded
                }
        ) {
            // 헤더 부분
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 18.dp,
                        top = 22.dp,
                        bottom = 22.dp,
                        end = 16.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$number. ${tip.title}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Grey600,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "접기" else "펼치기",
                    tint = Grey600
                )
            }
        }
        // 확장된 내용
        if (isExpanded) {
            Spacer(Modifier.height(9.dp))
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Grey100
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 13.dp,
                            vertical = 22.dp,
                        )
                ) {
                    TipDetailItem(
                        modifier = Modifier.fillMaxWidth(),
                        tipDetail = tip
                    )
                }
            }
        }
    }
}

@Composable
fun TipDetailItem(
    modifier: Modifier = Modifier,
    tipDetail: TipResponseItem
) {
    Column(
        modifier = modifier
    ) {
        // tipInfoDetailRes 리스트가 비어있지 않은 경우
        if (tipDetail.tipInfoDetailRes.isNotEmpty()) {
            tipDetail.tipInfoDetailRes.forEachIndexed { index, tipInfoDetail ->
                if (index > 0) {
                    Spacer(Modifier.height(16.dp))
                }

                // 각 tipInfoDetail 아이템 표시
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.dot),
                        contentDescription = "점",
                    )
                    Spacer(Modifier.width(5.dp))
                    Column {
                        // itemTitle이 있는 경우 표시
                        tipInfoDetail.itemTitle?.let { title ->
                            if (title.isNotEmpty()) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Grey600
                                )
                                // itemContent도 있으면 간격 추가
                                if (!tipInfoDetail.itemContent.isNullOrEmpty()) {
                                    Spacer(Modifier.height(9.dp))
                                }
                            }
                        }

                        // itemContent가 있는 경우 표시
                        tipInfoDetail.itemContent?.let { content ->
                            if (content.isNotEmpty()) {
                                Text(
                                    text = content,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Grey600
                                )
                            }
                        }

                        // warning이 있는 경우 표시
                        tipInfoDetail.warning?.let { warning ->
                            if (warning.isNotEmpty()) {
                                Spacer(Modifier.height(9.dp))
                                Text(
                                    text = warning,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Warning
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // tipInfoDetailRes가 비어있는 경우 안내 메시지
            Text(
                text = "이 항목에 대한 상세 정보가 아직 준비되지 않았습니다.",
                style = MaterialTheme.typography.bodySmall,
                color = Grey400
            )
        }
    }
}