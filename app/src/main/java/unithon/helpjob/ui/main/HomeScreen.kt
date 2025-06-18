package unithon.helpjob.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.data.model.request.Steps
import unithon.helpjob.data.model.response.DocumentInfoRes
import unithon.helpjob.ui.components.DottedProgressBar
import unithon.helpjob.ui.components.HelpJobCheckbox
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Primary100
import unithon.helpjob.ui.theme.Primary200
import unithon.helpjob.ui.theme.Primary300
import unithon.helpjob.ui.theme.Primary400
import unithon.helpjob.ui.theme.Primary600
import unithon.helpjob.util.noRippleClickable



@Composable
fun HomeScreen(
    onNavigateToStepDetail: () -> Unit, // 🔄 stepId를 받도록 변경
    viewmodel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    // HorizontalPager 상태
    val pagerState = rememberPagerState(pageCount = { uiState.steps.size })

    // 현재 선택된 스텝은 pagerState.currentPage로 자동 관리
    val selectedStepIndex = pagerState.currentPage

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(Modifier.height(51.dp))

            // 환영인사
            Text(
                text = stringResource(R.string.welcome_message, uiState.nickname),
                style = MaterialTheme.typography.headlineLarge,
                color = Grey600
            )
            Spacer(Modifier.height(18.dp))

            // 진행바
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                DottedProgressBar(
                    progress = uiState.progressPercentage,
                    modifier = Modifier.fillMaxWidth(),
                    showTicks = true,
                    showPercentage = true
                )
            }
            Spacer(Modifier.height(26.dp))

            // HorizontalPager로 스텝 리스트
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(end = 43.dp),
                pageSpacing = 0.dp
            ) { page ->
                StepCard(
                    step = Steps.valueOf(uiState.steps[page].checkStep).uiStep ,
                    title = uiState.steps[page].stepInfoRes.title,
                    subTitle = uiState.steps[page].stepInfoRes.subtitle,
                    onClick = {
                        viewmodel.selectStep(uiState.steps[page])
                        onNavigateToStepDetail()
                    }
                )
            }
            Spacer(Modifier.height(28.dp))

            // 카테고리 (제출 서류, 유의사항)
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable {
                            viewmodel.selectCategory("제출 서류")
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "제출 서류",
                        style = MaterialTheme.typography.titleSmall,
                        color = if (uiState.selectedCategory == "제출 서류") Primary600 else Grey300
                    )
                    Spacer(Modifier.height(11.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(color = if (uiState.selectedCategory == "제출 서류") Primary600 else Grey300)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable {
                            viewmodel.selectCategory("유의사항")
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "유의사항",
                        style = MaterialTheme.typography.titleSmall,
                        color = if (uiState.selectedCategory == "유의사항") Primary600 else Grey300
                    )
                    Spacer(Modifier.height(11.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(color = if (uiState.selectedCategory == "유의사항") Primary600 else Grey300)
                    )
                }
            }
            Spacer(Modifier.height(31.dp))

            // 선택된 스텝의 내용 표시
            if (uiState.steps.isNotEmpty() && selectedStepIndex < uiState.steps.size) {
                val selectedStep = uiState.steps[selectedStepIndex]

                if (uiState.selectedCategory == "제출 서류") {
                    // 제출 서류 목록 표시
                    selectedStep.documentInfoRes.forEach { document ->
                        DocumentItem(
                            document = document,
                            onCheckedChange = {}
                        )
                        Spacer(Modifier.height(9.dp))
                    }
                } else {
                    // 유의사항 목록 표시
                    selectedStep.stepInfoRes.precautions.forEach { precaution ->
                        PrecautionItem(
                            modifier = Modifier.fillMaxWidth(),
                            precaution = precaution
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            Spacer(Modifier.height(100.dp))
        }
    }
}

@Composable
fun StepCard(
    step: String,
    title: String,
    subTitle: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary400
        ),
        modifier = Modifier
            .noRippleClickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .width(264.dp)
                .padding(
                    horizontal = 12.dp,
                    vertical = 13.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Primary200
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(
                                vertical = 7.dp,
                                horizontal = 17.dp
                            ),
                        text = step,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Grey600
                    )
                }
                Spacer(Modifier.height(9.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Grey600
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = Grey600
                )
            }
            Icon(
                painter = painterResource(R.drawable.arrow_forward),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun DocumentItem(document: DocumentInfoRes, onCheckedChange: (Boolean) -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (document.isChecked) Primary300 else Primary100
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 22.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HelpJobCheckbox(
                checked = document.isChecked,
                onCheckedChange = onCheckedChange
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = document.title,
                style = MaterialTheme.typography.titleMedium,
                color = Grey600,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PrecautionItem(
    modifier: Modifier = Modifier,
    precaution: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary100
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 15.dp,
                    horizontal = 18.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.exclamation_mark),
                tint = Color.Unspecified,
                contentDescription = "느낌표"
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = precaution,
                style = MaterialTheme.typography.titleSmall,
                color = Grey600,
            )
        }
    }
}