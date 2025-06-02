package unithon.helpjob.ui.onboarding.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import unithon.helpjob.R
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.Primary500

@Composable
fun AgreementSection(
    isAllChecked: Boolean,
    isServiceChecked: Boolean,
    isPrivacyChecked: Boolean,
    isAgeChecked: Boolean,
    onAllCheckedChange: (Boolean) -> Unit,
    onServiceCheckedChange: (Boolean) -> Unit,
    onPrivacyCheckedChange: (Boolean) -> Unit,
    onAgeCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // 전체동의 버튼 - OnboardingButton 사용
        OnboardingButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            mainTitle = stringResource(R.string.onboarding_agreement_setup_full_agreement),
            onClick = { onAllCheckedChange(!isAllChecked) },
            icon = R.drawable.ic_check,
            iconMainSpacer = 16.dp,
            contentPosition = Arrangement.Start,
            enabled = isAllChecked
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 서비스 이용약관 동의
        AgreementItem(
            text = stringResource(R.string.onboarding_agreement_setup_service_agreement),
            isChecked = isServiceChecked,
            onCheckedChange = onServiceCheckedChange,
            onViewDetail = { /* 세부내용 보기 구현 */ }
        )


        // 개인정보 수집·이용 동의
        AgreementItem(
            text = stringResource(R.string.onboarding_agreement_setup_info_agreement),
            isChecked = isPrivacyChecked,
            onCheckedChange = onPrivacyCheckedChange,
            onViewDetail = { /* 세부내용 보기 구현 */ }
        )


        // 만 14세 이상 확인
        AgreementItem(
            text = stringResource(R.string.onboarding_agreement_setup_age_agreement),
            isChecked = isAgeChecked,
            onCheckedChange = onAgeCheckedChange,
            onViewDetail = { /* 세부내용 보기 구현 */ }
        )
    }
}

@Composable
fun AgreementItem(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onViewDetail: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 11.dp, horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable { onCheckedChange(!isChecked) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "동의 체크",
                tint = if (isChecked) Primary500 else Grey500,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Grey500,
                    lineHeight = 18.sp
                )
            )
        }

        Text(
            text = stringResource(R.string.onboarding_agreement_setup_more),
            modifier = Modifier
                .clickable { onViewDetail() }
                .padding(horizontal = 8.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                color = Color(0xFF70737D),
                textDecoration = TextDecoration.Underline,
                lineHeight = 18.sp
            )
        )
    }
}