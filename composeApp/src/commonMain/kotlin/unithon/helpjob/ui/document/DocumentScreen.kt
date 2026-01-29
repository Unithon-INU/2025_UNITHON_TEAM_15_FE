package unithon.helpjob.ui.document

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_login_required_description
import helpjob.composeapp.generated.resources.document_login_required_title
import helpjob.composeapp.generated.resources.document_onboarding_description_1
import helpjob.composeapp.generated.resources.document_onboarding_description_2
import helpjob.composeapp.generated.resources.document_onboarding_title
import helpjob.composeapp.generated.resources.document_step_1_title
import helpjob.composeapp.generated.resources.document_step_2_title
import helpjob.composeapp.generated.resources.document_top_bar_title
import helpjob.composeapp.generated.resources.memo
import helpjob.composeapp.generated.resources.message
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.components.LoginRequiredScreen
import unithon.helpjob.ui.document.page.BasicInfoStep1Screen
import unithon.helpjob.ui.document.page.BasicInfoStep2Screen
import unithon.helpjob.ui.document.page.DocumentOnboardingScreen
import unithon.helpjob.ui.document.page.EmailCheckScreen
import unithon.helpjob.ui.document.page.FinishScreen
import unithon.helpjob.ui.document.page.WorkplaceInfo1Screen
import unithon.helpjob.ui.document.page.WorkplaceInfo2Screen
import unithon.helpjob.ui.document.page.WorkplaceInfo3Screen
import unithon.helpjob.ui.document.page.WorkplaceInfo4Screen

/**
 * 플랫폼별 뒤로가기 처리 (Android: BackHandler, iOS: no-op)
 */
@Composable
expect fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit)

/**
 * 서류 생성 화면 (KMP 공통)
 */
@Composable
fun DocumentScreen(
    viewModel: DocumentViewModel = koinViewModel(),
    onNavigateToSignIn: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    DocumentScreenImpl(viewModel, onNavigateToSignIn, snackbarHostState)
}

@Composable
private fun DocumentScreenImpl(
    viewModel: DocumentViewModel,
    onNavigateToSignIn: () -> Unit,
    snackbarHostState: SnackbarHostState
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isSubmitting by viewModel.isSubmitting.collectAsStateWithLifecycle()

    // 🆕 Guest Mode 체크 - ViewModel State 사용
    if (uiState.isGuest) {
        LoginRequiredScreen(
            title = stringResource(Res.string.document_login_required_title),
            description = stringResource(Res.string.document_login_required_description),
            onLoginClick = onNavigateToSignIn
        )
        return
    }

    val pagerState = rememberPagerState(pageCount = { 10 })
    val scope = rememberCoroutineScope()

    // 공통 뒤로가기 처리 로직
    val handleBack: () -> Unit = {
        scope.launch {
            if (pagerState.currentPage == 9) {
                // 완료 화면에서 뒤로가기 시 처음으로 초기화
                viewModel.resetUiState()
                pagerState.animateScrollToPage(0)
            } else if (pagerState.currentPage > 0) {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        }
    }

    // 🆕 에러 이벤트 처리 - Snackbar 표시
    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.collect { messageRes  ->
            snackbarHostState.showSnackbar(
                message = getString(messageRes)
            )
        }
    }

    // 🆕 성공 이벤트 처리 - 완료 화면으로 이동
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect {
            pagerState.animateScrollToPage(9) // 완료 화면으로 이동
        }
    }

    // 시스템 뒤로가기 처리 - TopBar 뒤로가기와 동일하게 작동
    PlatformBackHandler(enabled = pagerState.currentPage > 0, onBack = handleBack)

    val pages = listOf(
        // 온보딩1
        DocumentPage(
            content = {
                DocumentOnboardingScreen(
                    title = stringResource(Res.string.document_onboarding_title) ,
                    image = Res.drawable.memo,
                    description = stringResource(Res.string.document_onboarding_description_1),
                    currentPage = 1,
                    pageSize = 2,
                    onNext = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )
            }
        ),
        // 온보딩2
        DocumentPage(
            content = {
                DocumentOnboardingScreen(
                    title = stringResource(Res.string.document_onboarding_title) ,
                    image = Res.drawable.message,
                    description = stringResource(Res.string.document_onboarding_description_2),
                    currentPage = 2,
                    pageSize = 2,
                    onNext = {
                        scope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    }
                )
            }
        ),
        // 기본 정보 입력1
        DocumentPage(
            content = {
                BasicInfoStep1Screen(
                    modifier = Modifier.fillMaxSize(),
                    step = 1,
                    title = stringResource(Res.string.document_step_1_title),
                    nameValue = uiState.name,
                    onNameValueChange = {viewModel.updateName(it)},
                    foreignerNumberValue = uiState.foreignerNumber,
                    onForeignerNumberValueChange = {viewModel.updateForeignerNumber(it)},
                    majorValue = uiState.major,
                    onMajorValueChange = {viewModel.updateMajor(it)},
                    enabled = uiState.isBasicInfo1Valid,
                    onNext = {
                        scope.launch {
                            pagerState.animateScrollToPage(3)
                        }
                    }
                )
            }
        ),
        // 기본 정보 입력2
        DocumentPage(
            content = {
                BasicInfoStep2Screen(
                    modifier = Modifier.fillMaxSize(),
                    emailError = uiState.emailError,  // 추가
                    emailErrorMessage = uiState.emailErrorMessage,
                    step = 1,
                    title = stringResource(Res.string.document_step_1_title),
                    semesterValue = uiState.semester,
                    onSemesterValueChange = {viewModel.updateSemester(it)},
                    phoneNumberValue = uiState.phoneNumber,
                    onPhoneNumberValueChange = {viewModel.updatePhoneNumber(it)},
                    emailAddressValue = uiState.emailAddress,
                    onEmailAddressValueChange = {viewModel.updateEmailAddress(it)},
                    enabled = uiState.isBasicInfo2Valid,
                    onNext = {
                        scope.launch {
                            pagerState.animateScrollToPage(4)
                        }
                    }
                )
            }
        ),
        // 취업 예정 근무처 정보 입력1
        DocumentPage(
            content = {
                WorkplaceInfo1Screen(
                    modifier = Modifier.fillMaxSize(),
                    step = 2,
                    title = stringResource(Res.string.document_step_2_title),
                    companyNameValue = uiState.companyName,
                    onCompanyNameValueChange = {viewModel.updateCompanyName(it)},
                    businessRegisterNumberValue = uiState.businessRegisterNumber,
                    onBusinessRegisterNumberValueChange = {viewModel.updateBusinessRegisterNumber(it)},
                    categoryOfBusinessValue = uiState.categoryOfBusiness,
                    onCategoryOfBusinessValueChange = {viewModel.updateCategoryOfBusiness(it)},
                    enabled = uiState.isWorkplaceInfo1Valid,
                    onNext = {
                        scope.launch {
                            pagerState.animateScrollToPage(5)
                        }
                    }
                )
            }
        ),
        // 취업 예정 근무처 정보 입력2
        DocumentPage(
            content = {
                WorkplaceInfo2Screen(
                    modifier = Modifier.fillMaxSize(),
                    step = 2,
                    title = stringResource(Res.string.document_step_2_title),
                    companyAddressValue = uiState.addressOfCompany,
                    onCompanyAddressValueChange = {viewModel.updateAddressOfCompany(it)},
                    employerNameValue = uiState.employerName,
                    onEmployerNameValueChange = {viewModel.updateEmployerName(it)},
                    employerPhoneNumberValue = uiState.employerPhoneNumber,
                    onEmployerPhoneNumberValueChange = {viewModel.updateEmployerPhoneNumber(it)},
                    enabled = uiState.isWorkplaceInfo2Valid,
                    onNext = {
                        scope.launch {
                            pagerState.animateScrollToPage(6)
                        }
                    }
                )
            }
        ),
        // 취업 예정 근무처 정보 입력3
        DocumentPage(
            content = {
                WorkplaceInfo3Screen(
                    modifier =Modifier.fillMaxSize(),
                    step = 2,
                    title = stringResource(Res.string.document_step_2_title),
                    hourlyWageValue = uiState.hourlyWage,
                    onHourlyWageValueChange = {viewModel.updateHourlyWage(it)},
                    workStartYearValue = uiState.workStartYear,
                    onWorkStartYearValueChange = {viewModel.updateWorkStartYear(it)},
                    workStartMonthValue = uiState.workStartMonth,
                    onWorkStartMonthValueChange = {viewModel.updateWorkStartMonth(it)},
                    workStartDayValue = uiState.workStartDay,
                    onWorkStartDayValueChange = {viewModel.updateWorkStartDay(it)},
                    workEndYearValue = uiState.workEndYear,
                    onWorkEndYearValueChange = {viewModel.updateWorkEndYear(it)},
                    workEndMonthValue = uiState.workEndMonth,
                    onWorkEndMonthValueChange = {viewModel.updateWorkEndMonth(it)},
                    workEndDayValue = uiState.workEndDay,
                    onWorkEndDayValueChange = {viewModel.updateWorkEndDay(it)},
                    enabled = uiState.isWorkplaceInfo3Valid,
                    onNext = {
                        scope.launch {
                            pagerState.animateScrollToPage(7)
                        }
                    }
                )
            }
        ),
        // 취업 예정 근무처 정보 입력4
        DocumentPage(
            content = {
                WorkplaceInfo4Screen(
                    modifier = Modifier.fillMaxSize(),
                    step = 2,
                    title = stringResource(Res.string.document_step_2_title),
                    workDays = uiState.workDays,
                    onWorkDayChange = { workDay -> viewModel.updateWorkDay(workDay) },
                    workDayTimes = uiState.workDayTimes,
                    onWorkDayStartTimeChange = { workDay, time ->
                        viewModel.updateWorkDayStartTime(workDay, time)
                    },
                    onWorkDayEndTimeChange = { workDay, time ->
                        viewModel.updateWorkDayEndTime(workDay, time)
                    },
                    isVacation = uiState.isVacation,
                    onToggleVacation = { viewModel.toggleVacation() },
                    isSameTimeForAll = uiState.isSameTimeForAll,
                    onToggleSameTimeForAll = { viewModel.toggleSameTimeForAll() },
                    weekdayTotalHours = uiState.weekdayTotalHours,
                    weekendTotalHours = uiState.weekendTotalHours,
                    isWeekdayOvertime = uiState.isWeekdayOvertime,
                    isWeekendOvertime = uiState.isWeekendOvertime,
                    enabled = uiState.isWorkplaceInfo4Valid,
                    onNext = {
                        scope.launch {
                            pagerState.animateScrollToPage(8)
                        }
                    }
                )
            }
        ),
        // 이메일 재확인 (🆕 수정: 페이지 이동 로직 제거)
        DocumentPage(
            content = {
                EmailCheckScreen(
                    modifier = Modifier.fillMaxSize(),
                    emailAddressValue = uiState.emailAddress,
                    emailAddressValueChange = { viewModel.updateEmailAddress(it) },
                    emailError = uiState.emailError,  // 🆕 추가
                    emailErrorMessage = uiState.emailErrorMessage,  // 🆕 추가
                    enabled = uiState.isAllValid,
                    isSubmitting = isSubmitting, // 🆕 로딩 상태 전달
                    onNext = {
                        if (uiState.isAllValid) {
                            viewModel.submitDocument() // 🆕 제출만 수행, 페이지 이동은 successEvent에서 처리
                        }
                    }
                )
            }
        ),
        // 완료 화면
        DocumentPage(
            content = {
                FinishScreen(
                    modifier = Modifier.fillMaxSize(),
                    onNext = {
                        viewModel.resetUiState()
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                )
            }
        ),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // 온보딩 화면(페이지 0, 1)에서는 TopBar 숨김
        if (pagerState.currentPage >= 2) {
            HelpJobTopAppBar(
                title = Res.string.document_top_bar_title,
                onBack = handleBack
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 페이저
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) { position ->
                pages[position].content()
            }
        }
    }
}

data class DocumentPage(
    val content: @Composable () -> Unit
)
