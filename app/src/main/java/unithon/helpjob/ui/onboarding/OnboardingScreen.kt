package unithon.helpjob.ui.onboarding

import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import timber.log.Timber
import unithon.helpjob.R
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.data.model.Business
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.onboarding.components.AgreementSection
import unithon.helpjob.ui.onboarding.components.OnboardingButton
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.Primary500

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onOnboardingComplete: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current


    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isOnboardingSuccess) {
        if (uiState.isOnboardingSuccess) {
            onOnboardingComplete()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.snackBarMessageResId.collect { messageResId ->
            val message = context.getString(messageResId)
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    val languageList = AppLanguage.entries.map { appLanguage ->
        OnboardingData(
            mainTitle = appLanguage.displayName
        )
    }
    val koreanLevelList = listOf(
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_korean_level_setup_topik3),
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_korean_level_setup_topik4_over),
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_korean_level_setup_no_topik),
        ),
    )
    val visaList = listOf(
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_visa_setup_d2_title),
            subTitle = stringResource(R.string.onboarding_visa_setup_d2_description)
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_visa_setup_d4_title),
            subTitle = stringResource(R.string.onboarding_visa_setup_d4_description)
        ),
    )
    val businessList = Business.entries.map { business ->
        OnboardingData(
            mainTitle = stringResource(business.displayNameResId)  // Enum의 StringRes 사용
        )
    }


    val pages = listOf(
        //언어 선택
        OnboardingPage(
            title = stringResource(R.string.onboarding_language_setup_title),
            content = {
                languageList.forEachIndexed { index, language ->
                    OnboardingButton(
                        modifier = Modifier
                            .height(46.dp)
                            .fillMaxWidth(),
                        mainTitle = language.mainTitle,
                        onClick = {
                            viewModel.updateLanguage(language.mainTitle)
                        },
                        icon = R.drawable.ic_check,
                        enabled = uiState.language == language.mainTitle
                    )
                    if (index < languageList.size - 1) {
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        ),
        //필수 약관 동의
        OnboardingPage(
            title = stringResource(R.string.onboarding_agreement_setup_title),
            content = {
                AgreementSection(
                    isAllChecked = uiState.fullAgreement,
                    isServiceChecked = uiState.serviceAgreement,
                    isPrivacyChecked = uiState.privacyAgreement,
                    isAgeChecked = uiState.ageAgreement,
                    onAllCheckedChange = { viewModel.updateAgreement(it) },
                    onServiceCheckedChange = { viewModel.updateServiceAgreement(it) },
                    onPrivacyCheckedChange = { viewModel.updatePrivacyAgreement(it) },
                    onAgeCheckedChange = { viewModel.updateAgeAgreement(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        ),
        //비자 선택
        OnboardingPage(
            title = stringResource(R.string.onboarding_visa_setup_title),
            content = {
                visaList.forEachIndexed { index, visa ->
                    OnboardingButton(
                        modifier = Modifier
                            .height(62.dp)
                            .fillMaxWidth(),
                        mainTitle = visa.mainTitle,
                        subTitle = visa.subTitle,
                        onClick = {
                            viewModel.updateVisa(visa.mainTitle)
                        },
                        contentPosition = Arrangement.Center,
                        enabled = uiState.visa == visa.mainTitle,
                    )
                    if (index < visaList.size - 1) {
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        ),
        //한국어 능력 선택
        OnboardingPage(
            title = stringResource(R.string.onboarding_korean_level_setup_title),
            content = {
                koreanLevelList.forEachIndexed { index, koreanLevel ->
                    OnboardingButton(
                        modifier = Modifier
                            .height(62.dp)
                            .fillMaxWidth(),
                        mainTitle = koreanLevel.mainTitle,
                        subTitle = koreanLevel.subTitle,
                        onClick = {
                            viewModel.updateKoreanLevel(koreanLevel.mainTitle)
                        },
                        contentPosition = Arrangement.Center,
                        enabled = uiState.koreanLevel == koreanLevel.mainTitle,
                    )
                    if (index < koreanLevelList.size - 1) {
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        ),
        //업종 선택
        OnboardingPage(
            title = stringResource(R.string.onboarding_business_setup_title),
            content = {
                LazyColumn{
                    itemsIndexed(businessList){ index, business->
                        OnboardingButton(
                            modifier = Modifier
                                .height(46.dp)
                                .fillMaxWidth(),
                            mainTitle = business.mainTitle,
                            onClick = {
                                viewModel.updateBusiness(business.mainTitle)
                            },
                            enabled = business.mainTitle in uiState.businesses
                        )
                        if (index < businessList.size - 1) {
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                }
            }
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
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
    Scaffold(
        topBar = {
            HelpJobTopAppBar(
                title = R.string.onboarding_top_bar_title,
                onBack = {
                    if (pagerState.currentPage > 0) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.padding(16.dp)  // 여백 추가로 더 잘 보이게
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 페이저
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) { position ->
                OnboardingPage(
                    page = pages[position],
                    pageCount = pages.size,
                    currentPage = position,
                    onNextPage = {
                        if (position < pages.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(position + 1)
                            }
                        }
                    },
                    onGetStarted = {
                        viewModel.completeOnboarding()
                    },
                    isValid = isCurrentPageValid
                )
            }

            if (uiState.userProfileError && uiState.userProfileErrorMessage != null) {
                LaunchedEffect(uiState.userProfileErrorMessage) {

                }
            }
        }
    }
}

@Composable
fun OnboardingPage(
    page: OnboardingPage,
    currentPage: Int,
    pageCount: Int,
    isValid: Boolean,
    onNextPage: () -> Unit,
    onGetStarted: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(15.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding()
                .align(Alignment.TopCenter),
        ) {
            CustomProgressIndicator(
                progress = (currentPage + 1).toFloat() / pageCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            )
            Spacer(Modifier.height(44.dp))
            Text(
                text = page.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 22.sp,
                    lineHeight = 32.sp
                ),
                color = Grey700
            )
            Spacer(Modifier.height(39.dp))
            page.content()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            HelpJobButton(
                text = if (currentPage == pageCount - 1) stringResource(R.string.onboarding_done_button) else stringResource(R.string.onboarding_next_button),
                onClick = {
                    if (currentPage == pageCount - 1) onGetStarted() else onNextPage()
                },
                enabled = isValid,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
        }

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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun OnboardingPreview(){
    val languageList = listOf(
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_language_setup_korean)
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_language_setup_english)
        )
    )
    val koreanLevelList = listOf(
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_korean_level_setup_topik3),
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_korean_level_setup_topik4_over),
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_korean_level_setup_no_topik),
        ),
    )
    val visaList = listOf(
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_visa_setup_d2_title),
            subTitle = stringResource(R.string.onboarding_visa_setup_d2_description)
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_visa_setup_d4_title),
            subTitle = stringResource(R.string.onboarding_visa_setup_d4_description)
        ),
    )
    val businessList = listOf(
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_business_setup_restaurant)
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_business_setup_mart)
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_business_setup_logistics)
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_business_setup_office)
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_business_setup_translation)
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_business_setup_learn)
        ),
        OnboardingData(
            mainTitle = stringResource(R.string.onboarding_business_setup_event)
        )
    )


    val pages = listOf(
        //언어 선택
        OnboardingPage(
            title = stringResource(R.string.onboarding_language_setup_title),
            content = {
                languageList.forEachIndexed { index, language ->
                    OnboardingButton(
                        modifier = Modifier
                            .height(46.dp)
                            .fillMaxWidth(),
                        mainTitle = language.mainTitle,
                        onClick = {
                        },
                        icon = R.drawable.ic_check,
                        enabled = false
                    )
                    if (index < languageList.size - 1) {
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        ),
        //필수 약관 동의
        OnboardingPage(
            title = stringResource(R.string.onboarding_agreement_setup_title),
            content = {
                AgreementSection(
                    isAllChecked = true,
                    isServiceChecked = true,
                    isPrivacyChecked = true,
                    isAgeChecked = true,
                    onAllCheckedChange = {  },
                    onServiceCheckedChange = {  },
                    onPrivacyCheckedChange = {  },
                    onAgeCheckedChange = {  },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        ),
        //한국어 능력 선택
        OnboardingPage(
            title = stringResource(R.string.onboarding_korean_level_setup_title),
            content = {
                koreanLevelList.forEachIndexed { index, koreanLevel ->
                    OnboardingButton(
                        modifier = Modifier
                            .height(62.dp)
                            .fillMaxWidth(),
                        mainTitle = koreanLevel.mainTitle,
                        subTitle = koreanLevel.subTitle,
                        onClick = {
                        },
                        contentPosition = Arrangement.Center,
                        enabled = true,
                    )
                    if (index < koreanLevelList.size - 1) {
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        ),
        //비자 선택
        OnboardingPage(
            title = stringResource(R.string.onboarding_visa_setup_title),
            content = {
                visaList.forEachIndexed { index, visa ->
                    OnboardingButton(
                        modifier = Modifier
                            .height(62.dp)
                            .fillMaxWidth(),
                        mainTitle = visa.mainTitle,
                        subTitle = visa.subTitle,
                        onClick = {
                        },
                        contentPosition = Arrangement.Center,
                        enabled = true,
                    )
                    if (index < visaList.size - 1) {
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        ),
        //업종 선택
        OnboardingPage(
            title = stringResource(R.string.onboarding_business_setup_title),
            content = {
                LazyColumn{
                    itemsIndexed(businessList){ index, business->
                        OnboardingButton(
                            modifier = Modifier
                                .height(46.dp)
                                .fillMaxWidth(),
                            mainTitle = business.mainTitle,
                            onClick = {
                            },
                            enabled = false
                        )
                        if (index < businessList.size - 1) {
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                }
            }
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    // 현재 페이지에 따른 유효성 검사 결과 계산
    val isCurrentPageValid = when (pagerState.currentPage) {
        0 -> true    // 언어 선택 페이지
        1 -> true    // 약관 동의 페이지
        2 -> true  // 한국어 능력 선택 페이지
        3 -> true         // 비자 선택 페이지
        4 -> true     // 업종 선택 페이지
        else -> false
    }

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
            OnboardingPage(
                page = pages[position],
                pageCount = pages.size,
                currentPage = position,
                onNextPage = {
                    if (position < pages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(position + 1)
                        }
                    }
                },
                onGetStarted = {
                },
                isValid = isCurrentPageValid
            )
        }
    }
}