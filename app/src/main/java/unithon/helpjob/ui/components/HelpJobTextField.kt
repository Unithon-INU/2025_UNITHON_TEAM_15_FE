package unithon.helpjob.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.ui.theme.*

@Composable
fun HelpJobTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    isPassword: Boolean = false // 🆕 비밀번호 필드 여부
) {
    var passwordVisible by remember { mutableStateOf(false) }

    // 🆕 비밀번호 필드인 경우 visibility 상태에 따라 transformation 결정
    val actualVisualTransformation = if (isPassword) {
        if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    } else {
        visualTransformation
    }

    Column(modifier = modifier) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = Grey500,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = if (placeholder.isNotBlank()) {
                {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Grey300
                        )
                    )
                }
            } else null,
            visualTransformation = actualVisualTransformation,
            keyboardOptions = keyboardOptions,
            isError = isError,
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            textStyle = MaterialTheme.typography.titleSmall.copy(
                color = Grey700
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = if (isError) Warning else Grey200,
                focusedBorderColor = if (isError) Warning else Primary500,
                errorBorderColor = Warning,
                cursorColor = Primary500,
                unfocusedContainerColor = Grey000,
                focusedContainerColor = Grey000,
                unfocusedTextColor = Grey700,
                focusedTextColor = Grey700
            ),
            // 🆕 비밀번호 토글 아이콘 추가
            trailingIcon = if (isPassword) {
                {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.eyeoff
                                else R.drawable.eyeon
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
            } else null,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
        )

        // 🆕 에러 메시지 필드 아래 직접 표시
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