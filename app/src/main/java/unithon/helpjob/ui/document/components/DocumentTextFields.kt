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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.PretendardFontFamily
import unithon.helpjob.ui.theme.Warning

/**
 * 문서 작성용 일반 텍스트필드 (이름, 전공, 회사명, 주소, 고용주명)
 */
@Composable
fun DocumentTextTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String,
    placeholderText: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next
) {
    Column(modifier = modifier) {
        if (labelText.isNotBlank()) {
            Text(
                text = labelText,
                style = MaterialTheme.typography.titleSmall,
                color = Grey500,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        HelpJobTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            placeholder = if (placeholderText.isNotBlank()) {
                {
                    Text(
                        text = placeholderText,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 17.sp,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Grey300
                        )
                    )
                }
            } else null,
            keyboardOptions = KeyboardOptions(
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
 * 문서 작성용 외국인등록번호 텍스트필드
 */
@Composable
fun DocumentForeignerNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String,
    placeholderText: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next
) {
    Column(modifier = modifier) {
        if (labelText.isNotBlank()) {
            Text(
                text = labelText,
                style = MaterialTheme.typography.titleSmall,
                color = Grey500,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        HelpJobTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            placeholder = if (placeholderText.isNotBlank()) {
                {
                    Text(
                        text = placeholderText,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 17.sp,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Grey300
                        )
                    )
                }
            } else null,
            visualTransformation = ForeignerNumberVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
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
 * 문서 작성용 전화번호 텍스트필드
 */
@Composable
fun DocumentPhoneNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String,
    placeholderText: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next
) {
    Column(modifier = modifier) {
        if (labelText.isNotBlank()) {
            Text(
                text = labelText,
                style = MaterialTheme.typography.titleSmall,
                color = Grey500,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        HelpJobTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            placeholder = if (placeholderText.isNotBlank()) {
                {
                    Text(
                        text = placeholderText,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 17.sp,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Grey300
                        )
                    )
                }
            } else null,
            visualTransformation = PhoneNumberVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
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
 * 문서 작성용 사업자등록번호 텍스트필드
 */
@Composable
fun DocumentBusinessNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String,
    placeholderText: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next
) {
    Column(modifier = modifier) {
        if (labelText.isNotBlank()) {
            Text(
                text = labelText,
                style = MaterialTheme.typography.titleSmall,
                color = Grey500,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        HelpJobTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            placeholder = if (placeholderText.isNotBlank()) {
                {
                    Text(
                        text = placeholderText,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 17.sp,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Grey300
                        )
                    )
                }
            } else null,
            visualTransformation = BusinessNumberVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
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
 * 문서 작성용 이메일 텍스트필드
 */
@Composable
fun DocumentEmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String,
    placeholderText: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next
) {
    Column(modifier = modifier) {
        if (labelText.isNotBlank()) {
            Text(
                text = labelText,
                style = MaterialTheme.typography.titleSmall,
                color = Grey500,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        HelpJobTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            placeholder = if (placeholderText.isNotBlank()) {
                {
                    Text(
                        text = placeholderText,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 17.sp,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Grey300
                        )
                    )
                }
            } else null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
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
 * 문서 작성용 시급 입력 텍스트필드
 */
@Composable
fun DocumentWageTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String,
    placeholderText: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next
) {
    Column(modifier = modifier) {
        if (labelText.isNotBlank()) {
            Text(
                text = labelText,
                style = MaterialTheme.typography.titleSmall,
                color = Grey500,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        HelpJobTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            placeholder = if (placeholderText.isNotBlank()) {
                {
                    Text(
                        text = placeholderText,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 17.sp,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Grey300
                        )
                    )
                }
            } else null,
            trailingIcon = {
                Text(
                    text = "원",
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey600
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
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
 * 문서 작성용 날짜 입력 텍스트필드 (년/월/일)
 */
@Composable
fun DocumentDateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    suffix: String = "", // "년", "월", "일"
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next
) {
    Column(modifier = modifier) {
        HelpJobTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            placeholder = if (placeholderText.isNotBlank()) {
                {
                    Text(
                        text = placeholderText,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 17.sp,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Grey300
                        )
                    )
                }
            } else null,
            trailingIcon = if (suffix.isNotBlank()) {
                {
                    Text(
                        text = suffix,
                        style = MaterialTheme.typography.titleSmall,
                        color = Grey600
                    )
                }
            } else null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
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