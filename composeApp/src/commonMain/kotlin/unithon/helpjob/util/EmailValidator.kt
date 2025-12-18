package unithon.helpjob.util

/**
 * 이메일 유효성 검증 유틸리티
 * RFC 5322 기반 정규식 사용 (모든 플랫폼 공통)
 *
 * Android Patterns.EMAIL_ADDRESS와 동등한 수준의 검증
 * 출처: Android Open Source Project
 */
object EmailValidator {
    // Android Patterns.EMAIL_ADDRESS와 동등한 수준의 검증
    private val EMAIL_REGEX = Regex(
        "[a-zA-Z0-9+._%−]{1,256}" +
        "@" +
        "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
        "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
        ")+"
    )

    /**
     * 이메일 주소 형식이 유효한지 검증
     * @param email 검증할 이메일 주소
     * @return 유효한 형식이면 true, 아니면 false
     */
    fun isValid(email: String): Boolean {
        return email.isNotEmpty() && EMAIL_REGEX.matches(email)
    }
}
