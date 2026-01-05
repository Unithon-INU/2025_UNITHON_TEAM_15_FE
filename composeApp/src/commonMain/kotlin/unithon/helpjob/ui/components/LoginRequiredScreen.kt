package unithon.helpjob.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.go_to_login
import helpjob.composeapp.generated.resources.login_required_description
import helpjob.composeapp.generated.resources.login_required_title
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.ui.theme.Grey400
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700

/**
 * 로그인 필요 화면 컴포넌트
 *
 * Guest Mode 사용자가 Member 전용 기능에 접근할 때 표시됩니다.
 * - 프로필 화면
 * - 시간제취업확인서 화면
 *
 * @param title 제목 (기본값: "로그인 후 이용 가능합니다")
 * @param description 설명 (기본값: "이 기능은 회원 전용 기능입니다...")
 * @param onLoginClick 로그인 버튼 클릭 시 콜백
 * @param modifier Modifier
 */
@Composable
fun LoginRequiredScreen(
    title: String = stringResource(Res.string.login_required_title),
    description: String = stringResource(Res.string.login_required_description),
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 잠금 아이콘
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Grey400
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 제목
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Grey700,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 설명
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Grey600,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 로그인 버튼
            HelpJobButton(
                text = stringResource(Res.string.go_to_login),
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }
    }
}
