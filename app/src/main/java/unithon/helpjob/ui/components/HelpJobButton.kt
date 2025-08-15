package unithon.helpjob.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey400
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Primary400
import unithon.helpjob.ui.theme.Primary500

/**
 * 버튼 색상 변형
 * 실제 프로젝트에서 사용되는 색상 조합만 정의
 */
enum class ButtonVariant {
    PRIMARY,    // Primary500 배경 - 주요 액션 (로그인, 저장 등)
    SECONDARY,  // Grey300 배경 - Dialog 취소 버튼
    TERTIARY    // Primary400 배경 - Dialog 확인 버튼
}

/**
 * HelpJob 프로젝트의 통합 버튼 컴포넌트 (주 버전 - StringRes 사용)
 *
 * StringRes를 받아서 내부적으로 stringResource()를 호출한 후
 * String 버전으로 위임합니다.
 *
 * @param textRes 버튼 텍스트 리소스 ID
 * @param onClick 클릭 이벤트 핸들러
 * @param modifier Modifier
 * @param variant 버튼 색상 변형 (기본값: PRIMARY)
 * @param enabled 활성화 상태 (기본값: true)
 * @param isLoading 로딩 상태 (기본값: false)
 * @param height 버튼 높이 (기본값: 46.dp - 기존과 동일)
 * @param contentPadding 내부 패딩 (기본값: vertical 13.dp)
 */
@Composable
fun HelpJobButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    height: Dp = 46.dp,  // 기존과 동일
    contentPadding: PaddingValues = PaddingValues(vertical = 13.dp)
) {
    HelpJobButton(
        text = stringResource(id = textRes),
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        enabled = enabled,
        isLoading = isLoading,
        height = height,
        contentPadding = contentPadding
    )
}

/**
 * String을 사용하는 실제 구현 버전
 *
 * 모든 실제 버튼 로직이 여기에 구현됩니다.
 * StringRes 버전은 이 함수로 위임합니다.
 *
 * @param text 버튼 텍스트 (동적 문자열)
 * @param onClick 클릭 이벤트 핸들러
 * @param modifier Modifier
 * @param variant 버튼 색상 변형
 * @param enabled 활성화 상태
 * @param isLoading 로딩 상태
 * @param height 버튼 높이
 * @param contentPadding 내부 패딩
 */
@Composable
fun HelpJobButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    height: Dp = 46.dp,  // 기존과 동일
    contentPadding: PaddingValues = PaddingValues(vertical = 13.dp)
) {
    val colors = getButtonColors(variant)

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier.height(height),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
            disabledContainerColor = colors.disabledContainerColor,
            disabledContentColor = colors.disabledContentColor
        ),
        contentPadding = contentPadding
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = colors.loadingColor,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium, // 16sp, Bold
                color = if (enabled) colors.contentColor else colors.disabledContentColor
            )
        }
    }
}

/**
 * 버튼 색상 정보를 담는 데이터 클래스
 */
private data class ButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val loadingColor: Color
)

/**
 * 버튼 변형에 따른 색상 반환
 *
 * @param variant 버튼 변형 타입
 * @return 해당 변형의 색상 세트
 */
@Composable
private fun getButtonColors(variant: ButtonVariant): ButtonColors {
    return when (variant) {
        ButtonVariant.PRIMARY -> ButtonColors(
            containerColor = Primary500,
            contentColor = Grey000,
            disabledContainerColor = Grey200,
            disabledContentColor = Grey400,
            loadingColor = Grey000
        )
        ButtonVariant.SECONDARY -> ButtonColors(
            containerColor = Grey300,  // StepProgressWarningDialog 취소 버튼
            contentColor = Grey500,  // 실제 Dialog와 동일
            disabledContainerColor = Grey200,
            disabledContentColor = Grey400,
            loadingColor = Grey500
        )
        ButtonVariant.TERTIARY -> ButtonColors(
            containerColor = Primary400,
            contentColor = Grey600,
            disabledContainerColor = Grey200,
            disabledContentColor = Grey400,
            loadingColor = Grey600
        )
    }
}

//@Composable
//fun HelpJobButton(
//    text: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    enabled: Boolean = true,
//    isLoading: Boolean = false,
//    contentPadding: PaddingValues = PaddingValues(vertical = 13.dp)
//) {
//    Button(
//        onClick = onClick,
//        enabled = enabled && !isLoading,
//        modifier = modifier.height(46.dp),
//        shape = RoundedCornerShape(10.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = if (enabled) Primary500 else Grey200,
//            contentColor = Grey000,
//            disabledContainerColor = Grey200,
//            disabledContentColor = Grey400
//        ),
//        contentPadding = contentPadding
//    ) {
//        if (isLoading) {
//            CircularProgressIndicator(
//                modifier = Modifier.size(24.dp),
//                color = Grey000,
//                strokeWidth = 2.dp
//            )
//        } else {
//            Text(
//                text = text,
//                style = MaterialTheme.typography.titleMedium, // 16sp, Bold
//                color = if (enabled) Grey000 else Grey400
//            )
//        }
//    }
//}