package unithon.helpjob.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import unithon.helpjob.R
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
    homeViewModel: HomeViewModel,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val homeUiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ProfileTopAppBar(
                onNavigateToSettings = onNavigateToSettings
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(top = 6.dp, start = 20.dp, end = 20.dp)
        ) {
            // 인사말 - 22sp Bold 커스텀 스타일 (기존과 동일)
            Text(
                text = stringResource(
                    id = R.string.profile_greeting,
                    homeUiState.nickname.ifEmpty { stringResource(R.string.profile_nickname_default) }
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
                    text = homeUiState.email.ifEmpty { stringResource(R.string.profile_email_default) },
                    style = MaterialTheme.typography.bodyLarge,
                    color = Grey500
                )

                Text(
                    text = stringResource(id = R.string.profile_email_signup_type),
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
                        label = stringResource(id = R.string.profile_visa_type),
                        value = uiState.visaType ?: stringResource(id = R.string.profile_visa_default),
                        modifier = Modifier.weight(1f)
                    )

                    VerticalDivider(
                        modifier = Modifier.height(71.dp),
                        thickness = 1.dp,
                        color = Grey300
                    )

                    ProfileInfoColumn(
                        label = stringResource(id = R.string.profile_korean_level),
                        value = uiState.topikLevel ?: stringResource(id = R.string.profile_korean_default),
                        modifier = Modifier.weight(1f)
                    )

                    VerticalDivider(
                        modifier = Modifier.height(71.dp),
                        thickness = 1.dp,
                        color = Grey300
                    )

                    ProfileInfoColumn(
                        label = stringResource(id = R.string.profile_preferred_job),
                        value = uiState.industry ?: stringResource(id = R.string.profile_job_default),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(39.dp))

            // 내 서류 관리 타이틀 (기존과 동일)
            Text(
                text = stringResource(id = R.string.profile_documents_title),
                style = MaterialTheme.typography.headline2, // 15sp Bold
                color = Grey700
            )
            Spacer(Modifier.height(5.dp))

            // 🆕 서류 관리 섹션만 추가
            DocumentManagementSection(
                homeUiState = homeUiState,
                onDocumentClick = { document ->
                    onNavigateToHomeWithStep(document.checkStep)
                }
            )
        }
    }
}

/**
 * 🆕 서류 관리 섹션 컴포넌트
 */
@Composable
private fun DocumentManagementSection(
    homeUiState: HomeViewModel.HomeUiState,
    onDocumentClick: (UncheckedDocument) -> Unit
) {
    // HomeUiState에서 실시간으로 누락된 서류 계산
    val uncheckedDocuments = remember(homeUiState.steps) {
        homeUiState.steps.flatMap { step ->
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
                    append(stringResource(id = R.string.profile_documents_all))
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
                    append(stringResource(id = R.string.profile_documents_completed))
                }
            }

            Text(
                text = allDocumentsText,
                style = MaterialTheme.typography.body2
            )

            Spacer(Modifier.height(12.dp))

            // 완료 상태 표시 박스
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Grey100, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.profile_documents_no_unchecked),
                    style = MaterialTheme.typography.body2,
                    color = Grey600
                )
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
                    append(stringResource(id = R.string.profile_documents_current))
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
                            id = R.string.profile_documents_count_format,
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
                    append(stringResource(id = R.string.profile_documents_not_checked))
                }
            }

            Text(
                text = uncheckedCountText,
                style = MaterialTheme.typography.body2
            )

            Spacer(Modifier.height(12.dp))

            uncheckedDocuments.forEach { document ->
                UncheckedDocumentItem(
                    document = document,
                    onClick = { onDocumentClick(document) }
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

/**
 * 🆕 누락된 서류 아이템 컴포넌트
 */
@Composable
private fun UncheckedDocumentItem(
    document: UncheckedDocument,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Grey100, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(
                id = R.string.profile_document_item_format,
                document.stepTitle,
                document.documentTitle
            ),
            style = MaterialTheme.typography.body4,
            color = Grey600
        )
    }
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
            style = MaterialTheme.typography.title2,
            color = Grey600
        )
    }
}