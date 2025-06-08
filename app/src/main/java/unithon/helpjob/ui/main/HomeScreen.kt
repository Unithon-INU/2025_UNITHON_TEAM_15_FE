package unithon.helpjob.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
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

data class Step(
    val step: Int,
    val title: String,
    val subTitle: String,
    val submissionDocument: List<Document>,
    val precautions: List<String>,
    val tips: List<Tip>
)

data class Document(
    val title: String,
    val isSuccess: Boolean
)

data class Tip(
    val title: String,
    val content: List<TipDetail>
)

data class TipDetail(
    val title: String,
    val content: String?,
    val warning: String?
)

@Composable
fun HomeScreen(
    onNavigateToStepDetail: (Int) -> Unit, // 🔄 stepId를 받도록 변경
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
                text = "반가워요 유니톤님\n함께 목표를 달성해요!",
                style = MaterialTheme.typography.headlineLarge,
                color = Grey600
            )
            Spacer(Modifier.height(18.dp))

            // 진행바
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                DottedProgressBar(
                    progress = 0.7f,
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
                    step = uiState.steps[page],
                    onClick = {
                        // 🔄 ViewModel에 저장하지 않고 바로 stepId 전달
                        onNavigateToStepDetail(uiState.steps[page].step)
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
                    selectedStep.submissionDocument.forEach { document ->
                        DocumentItem(
                            document = document,
                            onCheckedChange = {}
                        )
                        Spacer(Modifier.height(9.dp))
                    }
                } else {
                    // 유의사항 목록 표시
                    selectedStep.precautions.forEach { precaution ->
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
    step: Step,
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
                        text = "Step ${step.step}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Grey600
                    )
                }
                Spacer(Modifier.height(9.dp))
                Text(
                    text = step.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Grey600
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = step.subTitle,
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
fun DocumentItem(document: Document, onCheckedChange: (Boolean) -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (document.isSuccess) Primary300 else Primary100
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
                checked = document.isSuccess,
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