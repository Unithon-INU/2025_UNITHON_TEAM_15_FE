package unithon.helpjob.util

import java.util.Locale

/**
 * Android 플랫폼 숫자 포맷팅 구현
 * Locale.KOREA를 사용한 통화 포맷팅
 */
actual object NumberFormatter {
    actual fun formatCurrency(value: Long): String {
        return "${String.format(Locale.KOREA, "%,d", value)}원"
    }

    actual fun formatNumber(value: Int): String {
        return String.format(Locale.KOREA, "%,d", value)
    }
}
