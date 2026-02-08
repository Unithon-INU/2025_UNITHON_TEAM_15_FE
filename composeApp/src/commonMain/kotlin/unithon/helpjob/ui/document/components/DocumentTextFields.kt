package unithon.helpjob.ui.document.components

import BusinessNumberVisualTransformation
import ForeignerNumberVisualTransformation
import PhoneNumberVisualTransformation
import TimeVisualTransformation
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.Warning
import unithon.helpjob.ui.theme.title1
import unithon.helpjob.ui.theme.title2
import unithon.helpjob.util.CurrencyVisualTransformation

/**
 * Document 도메인에서 사용하는 통합 TextField
 *
 * 장점:
 * 1. 하나의 컴포넌트로 모든 Document TextField 요구사항 처리
 * 2. 유연한 설정 가능 (visualTransformation, keyboardType 등)
 * 3. 코드 중복 최소화
 * 4. 확장성 우수 (새로운 타입 추가 시 매개변수만 조정)
 * 5. 공식 문서 패턴 준수 (과도한 추상화 지양)
 */
@Composable
fun DocumentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String,
    placeholderText: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier) {
        // 🎯 Label을 TextField 외부에 별도로 배치
        if (labelText.isNotBlank()) {
            Text(
                text = labelText,
                style = MaterialTheme.typography.titleSmall,
                color = Grey600,
                modifier = Modifier.padding(bottom = 9.dp)
            )
        }

        // 🎯 TextField는 순수하게 입력 영역만 담당
        HelpJobTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            placeholder = if (placeholderText.isNotBlank()) {
                {
                    Text(
                        text = placeholderText,
                        style = MaterialTheme.typography.titleSmall,
                        color = Grey300
                    )
                }
            } else null,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()  // 무조건 키보드 숨김
                    onImeAction?.invoke()  // 콜백 실행 (있으면)
                }
                // onNext는 기본 동작 유지 (다음 필드로 포커스 이동)
            ),
            isError = isError
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Warning,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * 편의 함수들 - 타이핑 편의성을 위한 wrapper 함수들
 * 자주 사용되는 패턴들을 미리 정의해서 반복 작업을 줄임
 */

/**
 * 일반 텍스트 입력용 (이름, 전공, 회사명, 주소, 고용주명 등)
 */
@Composable
fun DocumentTextTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    placeholderText: String = "",
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) = DocumentTextField(
    value = value,
    onValueChange = onValueChange,
    labelText = labelText,
    placeholderText = placeholderText,
    modifier = modifier,
    imeAction = imeAction,
    onImeAction = onImeAction,
    isError = isError,
    errorMessage = errorMessage
)

/**
 * 이메일 입력용
 */
@Composable
fun DocumentEmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    placeholderText: String = "",
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) = DocumentTextField(
    value = value,
    onValueChange = onValueChange,
    labelText = labelText,
    placeholderText = placeholderText,
    keyboardType = KeyboardType.Email,
    modifier = modifier,
    imeAction = imeAction,
    onImeAction = onImeAction,
    isError = isError,
    errorMessage = errorMessage
)

/**
 * 전화번호 입력용
 */
@Composable
fun DocumentPhoneNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    placeholderText: String = "",
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) = DocumentTextField(
    value = value,
    onValueChange = onValueChange,
    labelText = labelText,
    placeholderText = placeholderText,
    visualTransformation = PhoneNumberVisualTransformation(),
    keyboardType = KeyboardType.Number,
    modifier = modifier,
    imeAction = imeAction,
    onImeAction = onImeAction,
    isError = isError,
    errorMessage = errorMessage
)

/**
 * 외국인등록번호 입력용
 */
@Composable
fun DocumentForeignerNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    placeholderText: String = "",
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    errorMessage: String? = null
) = DocumentTextField(
    value = value,
    onValueChange = onValueChange,
    labelText = labelText,
    placeholderText = placeholderText,
    visualTransformation = ForeignerNumberVisualTransformation(),
    keyboardType = KeyboardType.Number,
    modifier = modifier,
    imeAction = imeAction,
    isError = isError,
    errorMessage = errorMessage
)

/**
 * 사업자등록번호 입력용
 */
@Composable
fun DocumentBusinessNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    placeholderText: String = "",
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) = DocumentTextField(
    value = value,
    onValueChange = onValueChange,
    labelText = labelText,
    placeholderText = placeholderText,
    visualTransformation = BusinessNumberVisualTransformation(),
    keyboardType = KeyboardType.Number,
    modifier = modifier,
    imeAction = imeAction,
    onImeAction = onImeAction,
    isError = isError,
    errorMessage = errorMessage
)

/**
 * 시급 입력용
 */
@Composable
fun DocumentWageTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    placeholderText: String = "",
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    isError: Boolean = false,
    errorMessage: String? = null
) = DocumentTextField(
    value = value,
    onValueChange = onValueChange,
    labelText = labelText,
    placeholderText = placeholderText,
    visualTransformation = CurrencyVisualTransformation(),
    keyboardType = KeyboardType.Number,
    modifier = modifier,
    imeAction = imeAction,
    isError = isError,
    errorMessage = errorMessage
)

/**
 * 시간 입력용 (HH:MM 형식) - VisualTransformation 방식
 * 입력: 4자리 숫자 (예: 1330)
 * 표시: "13 : 30" 형식 (VisualTransformation으로 변환, 커서 정상 작동)
 */
@Composable
fun DocumentTimeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hourPlaceholder: String = "00",
    minutePlaceholder: String = "00",
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    nextFocusRequester: FocusRequester? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            // 숫자만 허용, 최대 4자리
            val filtered = newValue.filter { it.isDigit() }.take(4)

            // 시간 검증 (HH: 0-23, MM: 0-59)
            val isValid = if (filtered.length <= 2) {
                val hour = filtered.toIntOrNull() ?: 0
                hour <= 23 || filtered.length < 2
            } else {
                val hour = filtered.substring(0, 2).toIntOrNull() ?: 0
                val minute = filtered.substring(2).toIntOrNull() ?: 0
                hour <= 23 && (minute <= 59 || filtered.length < 4)
            }

            if (isValid) {
                onValueChange(filtered)

                // 4자리 입력 완료 시 다음 필드로 이동 또는 키보드 숨김
                if (filtered.length == 4) {
                    if (nextFocusRequester != null) {
                        nextFocusRequester.requestFocus()
                    } else if (imeAction == ImeAction.Done) {
                        keyboardController?.hide()
                    }
                    onImeAction?.invoke()
                }
            }
        },
        modifier = modifier
            .height(46.dp)
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
        textStyle = MaterialTheme.typography.title1.copy(
            color = Grey700,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onImeAction?.invoke()
            }
        ),
        singleLine = true,
        visualTransformation = TimeVisualTransformation(),
        cursorBrush = SolidColor(Grey700),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Grey000,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Grey200,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                // Placeholder: 디자인 스펙 (18dp start, : 양쪽 12dp, title2)
                if (value.isEmpty()) {
                    Row(
                        modifier = Modifier.padding(start = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = hourPlaceholder,
                            color = Grey300,
                            style = MaterialTheme.typography.title2
                        )
                        Text(
                            text = ":",
                            color = Grey300,
                            style = MaterialTheme.typography.title2,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Text(
                            text = minutePlaceholder,
                            color = Grey300,
                            style = MaterialTheme.typography.title2
                        )
                    }
                }

                // 입력 영역
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            }
        }
    )
}