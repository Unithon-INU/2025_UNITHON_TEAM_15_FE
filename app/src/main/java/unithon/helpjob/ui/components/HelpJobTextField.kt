package unithon.helpjob.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

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
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 16.sp, // 17sp에서 16sp로 조정 (텍스트 짤림 방지)
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Bold,
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
            textStyle = TextStyle(
                fontSize = 14.sp,
                lineHeight = 16.sp, // 17sp에서 16sp로 조정 (텍스트 짤림 방지)
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                color = Grey700
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = if (isError) Warning else Grey200,
                focusedBorderColor = if (isError) Warning else Grey200,
                errorBorderColor = Warning,
                cursorColor = Primary500,
                unfocusedContainerColor = Grey000,
                focusedContainerColor = Grey000,
                errorContainerColor = Grey000,
                unfocusedTextColor = Grey700,
                focusedTextColor = Grey700,
                errorTextColor = Grey700
            ),
            trailingIcon = if (isPassword) {
                {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
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
            } else null,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp) // 정확한 스펙에 맞게 46dp로 고정
        )

        // 에러 메시지
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