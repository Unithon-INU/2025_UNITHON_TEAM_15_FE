import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

// 외국인등록번호 VisualTransformation
class ForeignerNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val numbersOnly = text.text.filter { it.isDigit() }.take(13)

        val formatted = if (numbersOnly.length > 6) {
            "${numbersOnly.substring(0, 6)}-${numbersOnly.substring(6)}"
        } else {
            numbersOnly
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 6) return offset
                return offset + 1 // 하이픈 때문에 +1
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 6) return offset
                return offset - 1 // 하이픈 제거 -1
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

// 사업자등록번호 VisualTransformation
class BusinessNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val numbersOnly = text.text.filter { it.isDigit() }.take(10)

        val formatted = when {
            numbersOnly.length <= 3 -> numbersOnly
            numbersOnly.length <= 5 -> "${numbersOnly.substring(0, 3)}-${numbersOnly.substring(3)}"
            else -> "${numbersOnly.substring(0, 3)}-${numbersOnly.substring(3, 5)}-${numbersOnly.substring(5)}"
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 5 -> offset + 1
                    else -> offset + 2
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 5 -> offset - 1
                    else -> offset - 2
                }
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

// 전화번호 VisualTransformation
class PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val numbersOnly = text.text.filter { it.isDigit() }.take(11)

        val formatted = when {
            numbersOnly.length <= 3 -> numbersOnly
            numbersOnly.length <= 7 -> "${numbersOnly.substring(0, 3)}-${numbersOnly.substring(3)}"
            else -> "${numbersOnly.substring(0, 3)}-${numbersOnly.substring(3, 7)}-${numbersOnly.substring(7)}"
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 7 -> offset + 1
                    else -> offset + 2
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 7 -> offset - 1
                    else -> offset - 2
                }
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

