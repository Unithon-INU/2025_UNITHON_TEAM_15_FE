package unithon.helpjob.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val digitsOnly = originalText.filter { it.isDigit() }

        if (digitsOnly.isEmpty()) {
            return TransformedText(
                AnnotatedString(""),
                OffsetMapping.Identity
            )
        }

        val formattedText = addCommas(digitsOnly)

        return TransformedText(
            AnnotatedString(formattedText),
            CurrencyOffsetMapping(originalText, formattedText)
        )
    }

    private fun addCommas(number: String): String {
        if (number.isEmpty()) return number

        val result = StringBuilder()
        val reversedNumber = number.reversed()

        for (i in reversedNumber.indices) {
            if (i > 0 && i % 3 == 0) {
                result.append(',')
            }
            result.append(reversedNumber[i])
        }

        return result.reverse().toString()
    }

    override fun equals(other: Any?): Boolean {
        return other is CurrencyVisualTransformation
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}

class CurrencyOffsetMapping(
    private val originalText: String,
    private val transformedText: String
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        val digitsBeforeOffset = originalText.take(offset).count { it.isDigit() }
        var transformedOffset = 0
        var digitCount = 0

        for (i in transformedText.indices) {
            if (digitCount >= digitsBeforeOffset) break
            if (transformedText[i].isDigit()) {
                digitCount++
            }
            transformedOffset++
        }

        return transformedOffset.coerceAtMost(transformedText.length)
    }

    override fun transformedToOriginal(offset: Int): Int {
        val digitsBeforeOffset = transformedText.take(offset).count { it.isDigit() }
        var originalOffset = 0
        var digitCount = 0

        for (i in originalText.indices) {
            if (digitCount >= digitsBeforeOffset) break
            if (originalText[i].isDigit()) {
                digitCount++
            }
            originalOffset++
        }

        return originalOffset.coerceAtMost(originalText.length)
    }
}