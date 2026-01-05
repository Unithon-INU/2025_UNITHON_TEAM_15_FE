package unithon.helpjob.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.profile_documents_all
import helpjob.composeapp.generated.resources.profile_documents_completed
import helpjob.composeapp.generated.resources.profile_documents_count_format
import helpjob.composeapp.generated.resources.profile_documents_current
import helpjob.composeapp.generated.resources.profile_documents_no_unchecked
import helpjob.composeapp.generated.resources.profile_documents_not_checked
import helpjob.composeapp.generated.resources.profile_documents_title
import helpjob.composeapp.generated.resources.profile_email_default
import helpjob.composeapp.generated.resources.profile_email_signup_type
import helpjob.composeapp.generated.resources.profile_greeting
import helpjob.composeapp.generated.resources.profile_job_default
import helpjob.composeapp.generated.resources.profile_korean_default
import helpjob.composeapp.generated.resources.profile_korean_level
import helpjob.composeapp.generated.resources.profile_nickname_default
import helpjob.composeapp.generated.resources.profile_preferred_job
import helpjob.composeapp.generated.resources.profile_visa_default
import helpjob.composeapp.generated.resources.profile_visa_type
import helpjob.composeapp.generated.resources.profile_login_required_title
import helpjob.composeapp.generated.resources.profile_login_required_description
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.data.model.Business
import unithon.helpjob.data.model.TopikLevel
import unithon.helpjob.ui.main.HomeViewModel
import unithon.helpjob.ui.profile.components.ProfileTopAppBar
import unithon.helpjob.ui.theme.Blue500
import unithon.helpjob.ui.theme.Grey100
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey400
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.PretendardFontFamily
import unithon.helpjob.ui.theme.Warning
import unithon.helpjob.ui.theme.body2
import unithon.helpjob.ui.theme.body4
import unithon.helpjob.ui.theme.headline2
import unithon.helpjob.ui.theme.title2
import unithon.helpjob.ui.components.LoginRequiredScreen
import unithon.helpjob.util.noRippleClickable

/**
 * 누락된 서류 정보를 담는 데이터 클래스
 */
data class UncheckedDocument(
    val stepTitle: String,
    val documentTitle: String,
    val checkStep: String,
    val submissionIdx: Int
)

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit = {},
    onNavigateToHomeWithStep: (String) -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
    homeViewModel: HomeViewModel,
    snackbarHostState: SnackbarHostState,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val homeState by homeViewModel.homeState.collectAsState()

    // 🆕 Guest Mode 체크 - ViewModel State 사용
    val isGuest = uiState.isGuest

    // 🔥 언어 변경은 HomeViewModel에서 자동 처리 (여기서는 불필요)

    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.collect { messageRes ->
            snackbarHostState.showSnackbar(
                message = getString(messageRes)
            )
        }
    }

    // 🆕 Guest Mode일 경우 로그인 필요 화면 표시
    if (isGuest) {
        LoginRequiredScreen(
            title = stringResource(Res.string.profile_login_required_title),
            description = stringResource(Res.string.profile_login_required_description),
            onLoginClick = onNavigateToSignIn
        )
        return
    }

    // 기존 Member용 Profile UI
    Column(
        modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        ProfileTopAppBar(
            onNavigateToSettings = onNavigateToSettings
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 6.dp, start = 20.dp, end = 20.dp)
        ) {
            // 인사말 - 22sp Bold 커스텀 스타일 (기존과 동일)
            Text(
                text = stringResource(
                    Res.string.profile_greeting,
                    homeState.nickname.ifEmpty { stringResource(Res.string.profile_nickname_default) }
                ),
                style = TextStyle(
                    fontSize = 22.sp,
                    lineHeight = 32.sp,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Grey700
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 이메일 정보 Row - 양끝 정렬 (기존과 동일)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = homeState.email.ifEmpty { stringResource(Res.string.profile_email_default) },
                    style = MaterialTheme.typography.bodyLarge,
                    color = Grey500
                )

                Text(
                    text = stringResource(Res.string.profile_email_signup_type),
                    style = MaterialTheme.typography.bodyMedium, // 15sp Medium
                    color = Grey400
                )
            }

            Spacer(Modifier.height(24.dp))

            // 사용자 정보 카드 (기존과 동일)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Grey100,
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .padding(11.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileInfoColumn(
                        label = stringResource(Res.string.profile_visa_type),
                        value = uiState.visaType
                            ?: stringResource(Res.string.profile_visa_default),
                        modifier = Modifier.weight(1f)
                    )

                    VerticalDivider(
                        modifier = Modifier.height(71.dp),
                        thickness = 1.dp,
                        color = Grey300
                    )

                    ProfileInfoColumn(
                        label = stringResource(Res.string.profile_korean_level),
                        value = formatTopikLevelForDisplay(uiState.topikLevel),
                        modifier = Modifier.weight(1f)
                    )

                    VerticalDivider(
                        modifier = Modifier.height(71.dp),
                        thickness = 1.dp,
                        color = Grey300
                    )

                    ProfileInfoColumn(
                        label = stringResource(Res.string.profile_preferred_job),
                        value = formatIndustryForDisplay(uiState.industry),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(39.dp))

            // 내 서류 관리 타이틀 (기존과 동일)
            Text(
                text = stringResource(Res.string.profile_documents_title),
                style = MaterialTheme.typography.headline2, // 15sp Bold
                color = Grey700
            )
            Spacer(Modifier.height(5.dp))

            // 🆕 서류 관리 섹션만 추가
            DocumentManagementSection(
                homeState = homeState,
                onDocumentClick = { document ->
                    onNavigateToHomeWithStep(document.checkStep)
                }
            )
        }
    }
}

/**
 * 🆕 서류 관리 섹션 컴포넌트 - 그리드 레이아웃 적용
 */
@Composable
private fun DocumentManagementSection(
    homeState: unithon.helpjob.data.repository.HomeStateRepository.HomeState,
    onDocumentClick: (UncheckedDocument) -> Unit
) {
    // HomeState에서 실시간으로 누락된 서류 계산
    val uncheckedDocuments = remember(homeState.steps) {
        homeState.steps.flatMap { step ->
            step.documentInfoRes
                .filter { !it.isChecked }
                .map { document ->
                    UncheckedDocument(
                        stepTitle = step.stepInfoRes.title,
                        documentTitle = document.title,
                        checkStep = step.checkStep,
                        submissionIdx = document.submissionIdx
                    )
                }
        }
    }

    Column {
        if (uncheckedDocuments.isEmpty()) {
            // 모든 서류 완료 시 - "모든 서류"만 Blue500, "를 준비했어요!"는 Grey500
            val allDocumentsText = buildAnnotatedString {
                // "모든 서류" 부분 - Blue500
                withStyle(
                    style = SpanStyle(
                        color = Blue500,
                        fontSize = MaterialTheme.typography.body2.fontSize,
                        fontWeight = MaterialTheme.typography.body2.fontWeight,
                        fontFamily = MaterialTheme.typography.body2.fontFamily
                    )
                ) {
                    append(stringResource(Res.string.profile_documents_all))
                }

                // "를 준비했어요!" 부분 - Grey500
                withStyle(
                    style = SpanStyle(
                        color = Grey500,
                        fontSize = MaterialTheme.typography.body2.fontSize,
                        fontWeight = MaterialTheme.typography.body2.fontWeight,
                        fontFamily = MaterialTheme.typography.body2.fontFamily
                    )
                ) {
                    append(stringResource(Res.string.profile_documents_completed))
                }
            }

            Text(
                text = allDocumentsText,
                style = MaterialTheme.typography.body2
            )

            Spacer(Modifier.height(68.dp))

            // 완료 상태 표시 박스
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(Grey100, RoundedCornerShape(10.dp))
                        .padding(horizontal = 28.dp, vertical = 15.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.profile_documents_no_unchecked),
                        style = MaterialTheme.typography.body2,
                        color = Grey500
                    )
                }
            }
        } else {
            // 누락된 서류가 있는 경우 - "%d가지 서류"만 Warning, 나머지는 Grey500
            val uncheckedCountText = buildAnnotatedString {
                // "현재 " 부분 - Grey500
                withStyle(
                    style = SpanStyle(
                        color = Grey500,
                        fontSize = MaterialTheme.typography.body2.fontSize,
                        fontWeight = MaterialTheme.typography.body2.fontWeight,
                        fontFamily = MaterialTheme.typography.body2.fontFamily
                    )
                ) {
                    append(stringResource(Res.string.profile_documents_current))
                    append(" ")
                }

                // "%d가지 서류" 부분 - Warning (Alert 색상)
                withStyle(
                    style = SpanStyle(
                        color = Warning,
                        fontSize = MaterialTheme.typography.body2.fontSize,
                        fontWeight = MaterialTheme.typography.body2.fontWeight,
                        fontFamily = MaterialTheme.typography.body2.fontFamily
                    )
                ) {
                    append(
                        stringResource(
                            Res.string.profile_documents_count_format,
                            uncheckedDocuments.size
                        )
                    )
                }

                // "를 체크하지 않았어요" 부분 - Grey500
                withStyle(
                    style = SpanStyle(
                        color = Grey500,
                        fontSize = MaterialTheme.typography.body2.fontSize,
                        fontWeight = MaterialTheme.typography.body2.fontWeight,
                        fontFamily = MaterialTheme.typography.body2.fontFamily
                    )
                ) {
                    append(stringResource(Res.string.profile_documents_not_checked))
                }
            }

            Text(
                text = uncheckedCountText,
                style = MaterialTheme.typography.body2
            )

            Spacer(Modifier.height(24.dp))

            // 🆕 LazyVerticalGrid로 2열 그리드 적용
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uncheckedDocuments) { document ->
                    UncheckedDocumentItem(
                        document = document,
                        onClick = { onDocumentClick(document) }
                    )
                }
            }
        }
    }
}

/**
 * 🆕 누락된 서류 아이템 컴포넌트 - 그리드 레이아웃용
 */
@Composable
private fun UncheckedDocumentItem(
    document: UncheckedDocument,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .noRippleClickable { onClick() }
            .background(Grey100, RoundedCornerShape(10.dp))
            .padding(horizontal = 7.dp, vertical = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = document.documentTitle,
            style = MaterialTheme.typography.title2,
            color = Grey600,
            maxLines = 1, // 1줄까지 허용
            overflow = TextOverflow.Ellipsis, // 긴 텍스트는 ... 처리
        )
    }
}

@Composable
private fun formatIndustryForDisplay(industry: String?): String {
    if (industry.isNullOrEmpty()) {
        return stringResource(Res.string.profile_job_default)
    }

    // 1. 기존 로직: 쉼표로 구분된 여러 업종 처리
    val industries = industry.split(",").map { it.trim() }

    return when {
        industries.size <= 1 -> {
            // 2. 단일 업종인 경우: enum 매핑 적용
            val business = Business.fromDisplayText(industry)
            business?.displayNameRes?.let { res ->
                stringResource(res)
            } ?: industry
        }
        else -> {
            // 3. 여러 업종인 경우: 첫 번째만 enum 매핑하고 "..." 추가
            val firstIndustry = industries.first()
            val business = Business.fromDisplayText(firstIndustry)
            val displayName = business?.displayNameRes?.let { res ->
                stringResource(res)
            } ?: firstIndustry

            "$displayName ..."
        }
    }
}

@Composable
private fun formatTopikLevelForDisplay(topikLevel: String?): String {
    return topikLevel?.let { value ->
        val level = TopikLevel.fromDisplayText(value)
        stringResource(level.displayNameRes)
    } ?: stringResource(Res.string.profile_korean_default)
}


// 기존 ProfileInfoColumn 컴포넌트 (완전히 동일)
@Composable
private fun ProfileInfoColumn(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body4,
            color = Grey600
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = value,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.title2,
            color = Grey600,
            maxLines = 2, // 2줄까지 허용
            overflow = TextOverflow.Ellipsis, // 긴 텍스트는 ... 처리
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
