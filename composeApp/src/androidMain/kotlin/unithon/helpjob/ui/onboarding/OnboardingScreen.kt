package unithon.helpjob.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.compose.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import dev.icerock.moko.resources.desc.desc
import unithon.helpjob.R
import unithon.helpjob.resources.MR
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.data.model.Business
import unithon.helpjob.data.repository.GlobalLanguageState
import unithon.helpjob.data.repository.LanguageAwareScreen
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.onboarding.components.AgreementSection
import unithon.helpjob.ui.onboarding.components.OnboardingButton
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.Primary500

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    onOnboardingComplete: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val currentLanguage by GlobalLanguageState.currentLanguage


    LaunchedEffect(uiState.isOnboardingSuccess) {
        if (uiState.isOnboardingSuccess) {
            onOnboardingComplete()
        }
    }

    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.collect { messageRes ->
            snackbarHostState.showSnackbar(
                message = messageRes.desc().toString(context)
            )
        }
    }

    // 🔥 언어 상태를 remember 키로 사용하여 언어 변경 시 재계산되도록 수정
    val languageList = remember(currentLanguage) {
        AppLanguage.entries.map { appLanguage ->
            OnboardingData(
                mainTitle = appLanguage.displayName
            )
        }
    }

    val pagerState = rememberPagerState(pageCount = { 5 }) // 페이지 수 고정
    val scope = rememberCoroutineScope()

    // 현재 페이지에 따른 유효성 검사 결과 계산
    val isCurrentPageValid = when (pagerState.currentPage) {
        0 -> uiState.inLanguageValid     // 언어 선택 페이지
        1 -> uiState.isFullAgreementValid    // 약관 동의 페이지
        2 -> uiState.isVisaValid         // 비자 선택 페이지
        3 -> uiState.isKoreanLevelValid  // 한국어 능력 선택 페이지
        4 -> uiState.isBusinessValid     // 업종 선택 페이지
        else -> false
    }

    LanguageAwareScreen {
        Column(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            HelpJobTopAppBar(
                title = MR.strings.onboarding_top_bar_title,
                onBack = {
                    if (pagerState.currentPage > 0) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // 페이저
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    userScrollEnabled = false
                ) { position ->
                    OnboardingPageContainer(
                        pageIndex = position,
                        pageCount = 5,
                        currentLanguage = currentLanguage, // 🔥 언어 상태 전달
                        uiState = uiState,
                        viewModel = viewModel,
                        languageList = languageList,
                        isValid = isCurrentPageValid,
                        onNextPage = {
                            if (position < 4) {
                                scope.launch {
                                    pagerState.animateScrollToPage(position + 1)
                                }
                            }
                        },
                        onGetStarted = {
                            viewModel.completeOnboarding()
                        }
                    )
                }

                if (uiState.userProfileError && uiState.userProfileErrorMessage != null) {
                    LaunchedEffect(uiState.userProfileErrorMessage) {
                        // Handle error if needed
                    }
                }
            }
        }
    }
}

// 🔥 각 페이지를 별도 Composable로 분리하여 언어 변경 감지 향상
@Composable
private fun OnboardingPageContainer(
    pageIndex: Int,
    pageCount: Int,
    currentLanguage: AppLanguage, // 🔥 언어 상태 추가
    uiState: OnboardingViewModel.OnboardingUiState,
    viewModel: OnboardingViewModel,
    languageList: List<OnboardingData>,
    isValid: Boolean,
    onNextPage: () -> Unit,
    onGetStarted: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
        ) {
            Spacer(Modifier.height(15.dp))
            CustomProgressIndicator(
                progress = (pageIndex + 1).toFloat() / pageCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            )
            Spacer(Modifier.height(44.dp))

            // 🔥 페이지 제목 - currentLanguage를 remember 키로 사용
            val pageTitle = remember(currentLanguage, pageIndex) {
                when (pageIndex) {
                    0 -> MR.strings.onboarding_language_setup_title
                    1 -> MR.strings.onboarding_agreement_setup_title
                    2 -> MR.strings.onboarding_visa_setup_title
                    3 -> MR.strings.onboarding_korean_level_setup_title
                    4 -> MR.strings.onboarding_business_setup_title
                    else -> MR.strings.onboarding_language_setup_title
                }
            }

            Text(
                text = stringResource(pageTitle),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 22.sp,
                    lineHeight = 32.sp
                ),
                color = Grey700
            )
            Spacer(Modifier.height(39.dp))

            // 🔥 페이지별 컨텐츠
            when (pageIndex) {
                0 -> LanguageSelectionContent(
                    languageList = languageList,
                    selectedLanguage = uiState.language,
                    onLanguageSelected = { language ->
                        viewModel.updateLanguage(language)
                        GlobalLanguageState.updateLanguage(AppLanguage.fromDisplayName(language))
                    }
                )
                1 -> AgreementContent(
                    uiState = uiState,
                    viewModel = viewModel
                )
                2 -> VisaSelectionContent(
                    currentLanguage = currentLanguage,
                    selectedVisa = uiState.visa,
                    onVisaSelected = viewModel::updateVisa
                )
                3 -> KoreanLevelContent(
                    currentLanguage = currentLanguage,
                    selectedLevel = uiState.koreanLevel,
                    onLevelSelected = viewModel::updateKoreanLevel
                )
                4 -> BusinessSelectionContent(
                    currentLanguage = currentLanguage,
                    selectedBusinesses = uiState.businesses,
                    onBusinessSelected = viewModel::updateBusiness
                )
            }
        }

        // 🔥 하단 버튼 - currentLanguage를 remember 키로 사용
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            val buttonText = remember(currentLanguage, pageIndex) {
                if (pageIndex == pageCount - 1) {
                    MR.strings.onboarding_done_button
                } else {
                    MR.strings.onboarding_next_button
                }
            }

            HelpJobButton(
                text = stringResource(buttonText),
                onClick = {
                    if (pageIndex == pageCount - 1) onGetStarted() else onNextPage()
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}

// 🔥 언어 선택 컨텐츠
@Composable
private fun LanguageSelectionContent(
    languageList: List<OnboardingData>,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    languageList.forEachIndexed { index, language ->
        OnboardingButton(
            modifier = Modifier
                .height(46.dp)
                .fillMaxWidth(),
            mainTitle = language.mainTitle,
            onClick = { onLanguageSelected(language.mainTitle) },
            icon = R.drawable.ic_check,
            enabled = selectedLanguage == language.mainTitle
        )
        if (index < languageList.size - 1) {
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

// 🔥 약관 동의 컨텐츠
@Composable
private fun AgreementContent(
    uiState: OnboardingViewModel.OnboardingUiState,
    viewModel: OnboardingViewModel
) {
    AgreementSection(
        isAllChecked = uiState.fullAgreement,
        isServiceChecked = uiState.serviceAgreement,
        isPrivacyChecked = uiState.privacyAgreement,
        isAgeChecked = uiState.ageAgreement,
        onAllCheckedChange = viewModel::updateAgreement,
        onServiceCheckedChange = viewModel::updateServiceAgreement,
        onPrivacyCheckedChange = viewModel::updatePrivacyAgreement,
        onAgeCheckedChange = viewModel::updateAgeAgreement,
        modifier = Modifier.fillMaxWidth()
    )
}

// 🔥 비자 선택 컨텐츠
@Composable
private fun VisaSelectionContent(
    currentLanguage: AppLanguage,
    selectedVisa: String,
    onVisaSelected: (String) -> Unit
) {
    // 🔥 언어 변경 시 재계산되도록 remember 키 추가
    val visaList = remember(currentLanguage) {
        listOf(
            OnboardingData(
                mainTitle = "", // stringResource로 대체될 예정
                subTitle = ""
            ),
            OnboardingData(
                mainTitle = "",
                subTitle = ""
            )
        )
    }

    visaList.forEachIndexed { index, _ ->
        val (mainTitle, subTitle) = when (index) {
            0 -> stringResource(MR.strings.onboarding_visa_setup_d2_title) to
                    stringResource(MR.strings.onboarding_visa_setup_d2_description)
            else -> stringResource(MR.strings.onboarding_visa_setup_d4_title) to
                    stringResource(MR.strings.onboarding_visa_setup_d4_description)
        }

        OnboardingButton(
            modifier = Modifier
                .height(62.dp)
                .fillMaxWidth(),
            mainTitle = mainTitle,
            subTitle = subTitle,
            onClick = { onVisaSelected(mainTitle) },
            contentPosition = Arrangement.Center,
            enabled = selectedVisa == mainTitle,
        )
        if (index < 1) {
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

// 🔥 한국어 레벨 선택 컨텐츠
@Composable
private fun KoreanLevelContent(
    currentLanguage: AppLanguage,
    selectedLevel: String,
    onLevelSelected: (String) -> Unit
) {
    val koreanLevelTitles = listOf(
        stringResource(MR.strings.onboarding_korean_level_setup_topik3),
        stringResource(MR.strings.onboarding_korean_level_setup_topik4_over),
        stringResource(MR.strings.onboarding_korean_level_setup_no_topik)
    )

    koreanLevelTitles.forEachIndexed { index, title ->
        OnboardingButton(
            modifier = Modifier
                .height(62.dp)
                .fillMaxWidth(),
            mainTitle = title,
            onClick = { onLevelSelected(title) },
            contentPosition = Arrangement.Center,
            enabled = selectedLevel == title,
        )
        if (index < koreanLevelTitles.size - 1) {
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

// 🔥 업종 선택 컨텐츠
@Composable
private fun BusinessSelectionContent(
    currentLanguage: AppLanguage,
    selectedBusinesses: List<String>,
    onBusinessSelected: (String) -> Unit
) {
    // 🔥 언어 변경 시 재계산되도록 LocalContext 사용
    val context = LocalContext.current
    val businessList = remember(currentLanguage) {
        Business.entries.map { business ->
            OnboardingData(
                mainTitle = business.getDisplayName(context)
            )
        }
    }

    LazyColumn {
        itemsIndexed(businessList) { index, business ->
            OnboardingButton(
                modifier = Modifier
                    .height(46.dp)
                    .fillMaxWidth(),
                mainTitle = business.mainTitle,
                onClick = { onBusinessSelected(business.mainTitle) },
                enabled = business.mainTitle in selectedBusinesses
            )
            if (index < businessList.size - 1) {
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun CustomProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Grey300,
    progressColor: Color = Primary500
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .background(progressColor, shape = RoundedCornerShape(3.dp))
                .clip(RoundedCornerShape(3.dp))
        )
    }
}

data class OnboardingPage(
    val title: String,
    val content: @Composable () -> Unit,
)

data class OnboardingData(
    val mainTitle: String,
    val subTitle: String? = null
)