package unithon.helpjob.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

/**
 * iOS 플랫폼 숫자 포맷팅 구현
 * NSNumberFormatter를 사용한 통화 포맷팅
 */
actual object NumberFormatter {
    actual fun formatCurrency(value: Long): String {
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterDecimalStyle
        }
        val formattedNumber = formatter.stringFromNumber(NSNumber(value)) ?: value.toString()
        return "${formattedNumber}원"
    }
}
