package unithon.helpjob.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.data.model.request.Steps
import unithon.helpjob.data.model.response.DocumentInfoRes
import unithon.helpjob.data.repository.LanguageAwareScreen
import unithon.helpjob.ui.components.DottedProgressBar
import unithon.helpjob.ui.components.HelpJobCheckbox
import unithon.helpjob.ui.main.components.StepProgressWarningDialog
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Primary100
import unithon.helpjob.ui.theme.Primary200
import unithon.helpjob.ui.theme.Primary300
import unithon.helpjob.ui.theme.Primary400
import unithon.helpjob.ui.theme.Primary600
import unithon.helpjob.util.noRippleClickable

// HomeScreen.kt - 핵심 로직만 수정 (기존 컴포넌트는 그대로)

@Composable
fun HomeScreen(
    onNavigateToStepDetail: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var maxCardHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    // HorizontalPager 상태
    val pagerState = rememberPagerState(pageCount = { uiState.steps.size })

    // 🆕 초기화 완료 플래그 (첫 번째 데이터 로딩 후 자동 이동 방지용)
    var isInitialized by remember { mutableStateOf(false) }

    // 🆕 사용자가 수동으로 페이저를 조작했는지 추적
    var userHasInteracted by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.collect { messageRes ->
            snackbarHostState.showSnackbar(
                message = context.getString(messageRes)
            )
        }
    }

    // 첫 번째 데이터 로딩 완료 시 초기화 플래그 설정
    LaunchedEffect(uiState.steps.isNotEmpty()) {
        if (uiState.steps.isNotEmpty() && !isInitialized) {
            isInitialized = true
        }
    }

    // 🔥 핵심 수정: memberCheckStep 기반 이동은 초기화 시에만 실행
    LaunchedEffect(uiState.memberCheckStep, uiState.steps) {
        if (uiState.steps.isNotEmpty() && !isInitialized && !userHasInteracted) {
            val targetIndex = uiState.steps.indexOfFirst {
                it.checkStep == uiState.memberCheckStep.apiStep
            }
            if (targetIndex >= 0) {
                pagerState.scrollToPage(targetIndex)
            }
        }
    }

    // selectedStep 변경 시 pager 동기화 (우선순위 높음)
    LaunchedEffect(uiState.selectedStep) {
        uiState.selectedStep?.let { selectedStep ->
            val targetIndex = uiState.steps.indexOfFirst { it.checkStep == selectedStep.checkStep }
            if (targetIndex >= 0 && targetIndex != pagerState.currentPage) {
                userHasInteracted = true // 사용자 인터랙션으로 표시
                pagerState.scrollToPage(targetIndex)
            }
        }
    }

    // 🆕 사용자가 직접 페이저를 스와이프할 때 인터랙션 플래그 설정
    LaunchedEffect(pagerState.isScrollInProgress) {
        if (pagerState.isScrollInProgress && isInitialized) {
            userHasInteracted = true
        }
    }

    // ✅ 핵심 수정 3: displayStep 로직 개선 - 항상 최신 데이터 사용
    val displayStep = if (uiState.steps.isNotEmpty() && pagerState.currentPage < uiState.steps.size) {
        uiState.steps[pagerState.currentPage]  // 항상 현재 페이지의 최신 데이터 사용
    } else null

    val scrollState = rememberScrollState()
    LanguageAwareScreen {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
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

                // 🆕 최대 높이가 결정된 경우에만 HorizontalPager 표시
                if (maxCardHeight > 0.dp) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxCardHeight), // 🆕 동적으로 계산된 최대 높이 사용
                        contentPadding = PaddingValues(end = 43.dp),
                        pageSpacing = 0.dp
                    ) { page ->
                        StepCard(
                            step = Steps.valueOf(uiState.steps[page].checkStep).uiStep,
                            title = uiState.steps[page].stepInfoRes.title,
                            subTitle = uiState.steps[page].stepInfoRes.subtitle,
                            onClick = {
                                viewModel.selectStep(uiState.steps[page])
                                onNavigateToStepDetail()
                            },
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                } else {
                    // 🆕 높이 측정을 위한 임시 컴포저블들 (화면에 보이지 않음)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0f) // 투명하게 만들어 보이지 않게 함
                    ) {
                        uiState.steps.forEachIndexed { index, step ->
                            StepCard(
                                step = Steps.valueOf(step.checkStep).uiStep,
                                title = step.stepInfoRes.title,
                                subTitle = step.stepInfoRes.subtitle,
                                onClick = { },
                                modifier = Modifier
                                    .onGloballyPositioned { coordinates ->
                                        val height = with(density) { coordinates.size.height.toDp() }
                                        if (height > maxCardHeight) {
                                            maxCardHeight = height
                                        }
                                    }
                            )
                            if (index < uiState.steps.size - 1) {
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }
                Spacer(Modifier.height(28.dp))

                // 카테고리 (제출 서류, 유의사항) - stringResource 사용
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CategoryTab(
                        text = stringResource(R.string.category_documents),
                        isSelected = uiState.selectedCategory == HomeViewModel.Category.DOCUMENTS,
                        onClick = { viewModel.selectCategory(HomeViewModel.Category.DOCUMENTS) },
                        modifier = Modifier.weight(1f)
                    )
                    CategoryTab(
                        text = stringResource(R.string.category_precautions),
                        isSelected = uiState.selectedCategory == HomeViewModel.Category.PRECAUTIONS,
                        onClick = { viewModel.selectCategory(HomeViewModel.Category.PRECAUTIONS) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(31.dp))

                //  displayStep 기반으로 UI 표시 (기존 컴포넌트 그대로 사용)
                displayStep?.let { step ->
                    when (uiState.selectedCategory) {
                        HomeViewModel.Category.DOCUMENTS -> {
                            // 제출 서류 목록 표시 (기존 DocumentItem 그대로 사용)
                            step.documentInfoRes.forEach { document ->
                                DocumentItem(
                                    document = document,
                                    enabled = !uiState.isUpdating,
                                    onCheckedChange = { isChecked ->
                                        viewModel.onDocumentCheckChanged(
                                            document = document,
                                            stepCheckStep = step.checkStep,
                                            isChecked = isChecked
                                        )
                                    }
                                )
                                Spacer(Modifier.height(9.dp))
                            }
                        }
                        HomeViewModel.Category.PRECAUTIONS -> {
                            // 유의사항 목록 표시 (기존 PrecautionItem 그대로 사용)
                            step.stepInfoRes.precautions.forEach { precaution ->
                                PrecautionItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    precaution = precaution
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(100.dp))
            }

            // 경고 다이얼로그
            if (uiState.showStepWarningDialog) {
                StepProgressWarningDialog(
                    onDismiss = { viewModel.dismissStepWarningDialog() },
                    onContinue = { viewModel.continueWithCheck() }
                )
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun CategoryTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.noRippleClickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = if (isSelected) Primary600 else Grey300
        )
        Spacer(Modifier.height(11.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(color = if (isSelected) Primary600 else Grey300)
        )
    }
}

@Composable
fun StepCard(
    modifier: Modifier = Modifier,
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
        modifier = modifier
            .noRippleClickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .width(264.dp)
                .fillMaxHeight()
                .padding(
                    horizontal = 12.dp,
                    vertical = 13.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight()
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
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun DocumentItem(
    document: DocumentInfoRes,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
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
                enabled = enabled,
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
                contentDescription = null
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