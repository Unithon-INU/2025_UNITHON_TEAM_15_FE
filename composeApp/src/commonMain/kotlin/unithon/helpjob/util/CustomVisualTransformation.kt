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

// 시간 VisualTransformation (HH    :    MM 형식, 공백으로 16dp 근사치 패딩)
class TimeVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // 숫자만 추출, 최대 4자리
        val numbersOnly = text.text.filter { it.isDigit() }.take(4)

        // "HH    :    MM" 형식으로 포맷팅 (공백 4개씩으로 16dp 근사치)
        val formatted = when {
            numbersOnly.isEmpty() -> ""
            numbersOnly.length <= 2 -> numbersOnly
            else -> "${numbersOnly.substring(0, 2)}    :    ${numbersOnly.substring(2)}"
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // 2자리 이후에 "    :    " (9문자) 삽입됨
                return if (offset <= 2) offset else offset + 9
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset <= 11 -> 2  // "    :    " 구간에서는 2로 매핑
                    else -> offset - 9
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
