package unithon.helpjob.ui.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey400
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Warning

/**
 * Auth 도메인에서 사용하는 통합 TextField
 *
 * 장점:
 * 1. 하나의 컴포넌트로 모든 Auth TextField 요구사항 처리
 * 2. DocumentTextField와 동일한 패턴으로 일관성 유지
 * 3. 코드 중복 최소화
 * 4. 확장성 우수 (새로운 타입 추가 시 매개변수만 조정)
 * 5. 공식 문서 패턴 준수 (과도한 추상화 지양)
 */
@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String = "",
    placeholderText: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    errorMessage: String? = null,
    showPasswordToggle: Boolean = false,
    maxLength: Int? = null, // 글자수 제한 (null이면 제한 없음)
    showCharacterCount: Boolean = false, // 글자수 카운터 표시 여부
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    // 패스워드 토글이 활성화된 경우 전용 아이콘 사용, 아니면 전달받은 trailingIcon 사용
    val finalTrailingIcon: @Composable (() -> Unit)? = if (showPasswordToggle) {
        {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = painterResource(
                        id = if (passwordVisible) R.drawable.eyeon
                        else R.drawable.eyeoff
                    ),
                    contentDescription = if (passwordVisible) {
                        stringResource(R.string.hide_password)
                    } else {
                        stringResource(R.string.show_password)
                    },
                    tint = Grey400,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    } else {
        trailingIcon
    }

    // 패스워드 필드의 경우 가시성에 따라 VisualTransformation 결정
    val finalVisualTransformation = if (showPasswordToggle) {
        if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    Column(modifier = modifier) {
        HelpJobTextField(
            value = value,
            onValueChange = { newValue ->
                // 글자수 제한이 있는 경우 체크
                if (maxLength != null) {
                    if (newValue.length <= maxLength) {
                        onValueChange(newValue)
                    }
                } else {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            label = if (labelText.isNotBlank()) {
                {
                    Text(
                        text = labelText,
                        style = MaterialTheme.typography.titleSmall,
                        color = Grey500,
                        modifier = Modifier.padding(bottom = 9.dp)
                    )
                }
            } else null,
            placeholder = if (placeholderText.isNotBlank()) {
                {
                    Text(
                        text = placeholderText,
                        style = MaterialTheme.typography.titleSmall,
                        color = Grey300
                    )
                }
            } else null,
            trailingIcon = finalTrailingIcon,
            visualTransformation = finalVisualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            isError = isError
        )

        // 에러 메시지와 글자수 카운터를 함께 표시
        if ((isError && errorMessage != null) || showCharacterCount) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 에러 메시지 (왼쪽)
                if (isError && errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = Warning,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                // 글자수 카운터 (오른쪽)
                if (showCharacterCount && maxLength != null) {
                    Text(
                        text = "${value.length}/$maxLength",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (value.length == maxLength) Warning else Grey400,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

/**
 * 편의 함수들 - 타이핑 편의성을 위한 wrapper 함수들
 * 자주 사용되는 패턴들을 미리 정의해서 반복 작업을 줄임
 */

/**
 * 로그인/회원가입용 이메일 텍스트필드
 */
@Composable
fun AuthEmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String = "",
    placeholderText: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next
) = AuthTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    labelText = labelText,
    placeholderText = placeholderText,
    keyboardType = KeyboardType.Email,
    imeAction = imeAction,
    isError = isError,
    errorMessage = errorMessage
)

/**
 * 로그인/회원가입용 비밀번호 텍스트필드
 */
@Composable
fun AuthPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String = "",
    placeholderText: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next
) = AuthTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    labelText = labelText,
    placeholderText = placeholderText,
    keyboardType = KeyboardType.Password,
    imeAction = imeAction,
    isError = isError,
    errorMessage = errorMessage,
    showPasswordToggle = true
)

/**
 * 회원가입용 인증코드 입력 텍스트필드
 */
@Composable
fun AuthVerificationCodeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Done
) = AuthTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    placeholderText = placeholderText,
    keyboardType = KeyboardType.Number,
    imeAction = imeAction,
    isError = isError,
    errorMessage = errorMessage
)

/**
 * 닉네임 설정용 텍스트필드 (글자수 제한 + 카운터)
 */
@Composable
fun AuthNicknameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    maxLength: Int = 10, // 닉네임 최대 길이 (기본값 10자)
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Done
) = AuthTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    placeholderText = placeholderText,
    keyboardType = KeyboardType.Text,
    imeAction = imeAction,
    isError = isError,
    errorMessage = errorMessage,
    maxLength = maxLength,
    showCharacterCount = true
)