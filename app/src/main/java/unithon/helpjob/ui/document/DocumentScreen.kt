package unithon.helpjob.ui.document

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.document.page.BasicInfoStep1Screen
import unithon.helpjob.ui.document.page.BasicInfoStep2Screen
import unithon.helpjob.ui.document.page.DocumentOnboardingScreen
import unithon.helpjob.ui.document.page.EmailCheckScreen
import unithon.helpjob.ui.document.page.FinishScreen
import unithon.helpjob.ui.document.page.WorkplaceInfo1Screen
import unithon.helpjob.ui.document.page.WorkplaceInfo2Screen
import unithon.helpjob.ui.document.page.WorkplaceInfo3Screen
import unithon.helpjob.ui.document.page.WorkplaceInfo4Screen

@Composable
fun DocumentScreen(
    viewModel: DocumentViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { 10 })
    val scope = rememberCoroutineScope()

    val pages = listOf(
        // 온보딩1
        DocumentPage(
            content = {
                DocumentOnboardingScreen(
                    title = stringResource(R.string.document_onboarding_title) ,
                    image = R.drawable.memo,
                    description = stringResource(R.string.document_onboarding_description_1),
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
                    title = stringResource(R.string.document_onboarding_title) ,
                    image = R.drawable.message,
                    description = stringResource(R.string.document_onboarding_description_2),
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
                    title = stringResource(R.string.document_step_1_title),
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
                    step = 1,
                    title = stringResource(R.string.document_step_1_title),
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
                    title = stringResource(R.string.document_step_2_title),
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
                    title = stringResource(R.string.document_step_2_title),
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
                    title = stringResource(R.string.document_step_2_title),
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
                    title = stringResource(R.string.document_step_2_title),
                    workDays = uiState.workDays,
                    onWorkDayChange = { workDay -> viewModel.updateWorkDay(workDay) },
                    workDayTimes = uiState.workDayTimes,
                    onWorkDayStartTimeChange = { workDay, time ->
                        viewModel.updateWorkDayStartTime(workDay, time)
                    },
                    onWorkDayEndTimeChange = { workDay, time ->
                        viewModel.updateWorkDayEndTime(workDay, time)
                    },
                    isAllDaysSelected = uiState.isAllDaysSelected,
                    onToggleAllDays = { viewModel.toggleAllDays() },
                    isSameTimeForAll = uiState.isSameTimeForAll,
                    onToggleSameTimeForAll = { viewModel.toggleSameTimeForAll() },
                    enabled = uiState.isWorkplaceInfo4Valid,
                    onNext = {
                        scope.launch {
                            pagerState.animateScrollToPage(8)
                        }
                    }
                )
            }
        ),
        // 이메일 재확인
        DocumentPage(
            content = {
                EmailCheckScreen(
                    modifier =Modifier.fillMaxSize(),
                    emailAddressValue = uiState.emailAddress,
                    emailAddressValueChange = {viewModel.updateEmailAddress(it)},
                    enabled = uiState.isAllValid,
                    onNext = {
                        if (uiState.isAllValid) {
                            viewModel.submitDocument()
                            scope.launch {
                                pagerState.animateScrollToPage(9)
                            }
                        }
                    }
                )
            }
        ),
        // 완료 화면
        DocumentPage(
            content = {
                FinishScreen(
                    modifier =Modifier.fillMaxSize(),
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

    Scaffold(
        topBar = {
            // 온보딩 화면(페이지 0, 1)에서는 TopBar 숨김
            if (pagerState.currentPage >= 2) {
                HelpJobTopAppBar(
                    title = R.string.document_top_bar_title,
                    onBack = {
                        if (pagerState.currentPage > 0) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    }
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .then(
                    if (pagerState.currentPage >= 2) {
                        Modifier.padding(top = innerPadding.calculateTopPadding())
                    } else {
                        Modifier
                    }
                )
                .fillMaxSize()
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