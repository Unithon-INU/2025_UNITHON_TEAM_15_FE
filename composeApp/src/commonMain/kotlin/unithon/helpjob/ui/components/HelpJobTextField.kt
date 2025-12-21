package unithon.helpjob.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey400
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
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = TextStyle(
            fontSize = 14.sp,
            lineHeight = 17.sp,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold,
            color = if (enabled) Grey700 else Grey400
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(Primary500),
        decorationBox = @Composable { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Grey000,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isError) Warning else Grey200,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(
                        start = 16.dp,
                        top = 10.dp,
                        end = 16.dp,
                        bottom = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading Icon
                leadingIcon?.invoke()

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    // Placeholder
                    if (value.isEmpty() && placeholder != null) {
                        placeholder()
                    }

                    // Inner TextField
                    innerTextField()
                }

                // Trailing Icon
                trailingIcon?.invoke()
            }
        }
    )
}