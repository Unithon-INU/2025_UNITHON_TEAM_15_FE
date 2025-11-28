package unithon.helpjob.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.PretendardFontFamily
import unithon.helpjob.ui.theme.Primary500
import unithon.helpjob.ui.theme.Warning

@Composable
fun HelpJobTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = isError,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        textStyle = TextStyle(
            fontSize = 14.sp,
            lineHeight = 17.sp,
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
        )
    )
}