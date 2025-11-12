package unithon.helpjob.util

/**
 * 플랫폼별 숫자 포맷팅 유틸리티
 */
expect object NumberFormatter {
    /**
     * 통화 형식으로 포맷팅 (예: 10,030원)
     * @param value 포맷팅할 숫자
     * @return 포맷팅된 문자열 (예: "10,030원")
     */
    fun formatCurrency(value: Long): String
}
