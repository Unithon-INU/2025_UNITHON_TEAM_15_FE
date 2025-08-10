package unithon.helpjob.ui.document.components

import BusinessNumberVisualTransformation
import ForeignerNumberVisualTransformation
import PhoneNumberVisualTransformation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Warning
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
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = modifier) {
        HelpJobTextField(
            value = value,
            onValueChange = onValueChange,
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
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
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
    isError: Boolean = false,
    errorMessage: String? = null
) = DocumentTextField(
    value = value,
    onValueChange = onValueChange,
    labelText = labelText,
    placeholderText = placeholderText,
    modifier = modifier,
    imeAction = imeAction,
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