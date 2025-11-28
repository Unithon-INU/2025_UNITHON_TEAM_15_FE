package unithon.helpjob.util

/**
 * iOS 플랫폼 이메일 유효성 검증 구현
 * Regex 기반 검증 사용
 */
actual object EmailValidator {
    private val emailRegex = Regex(
        pattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
        option = RegexOption.IGNORE_CASE
    )

    actual fun isValid(email: String): Boolean {
        return emailRegex.matches(email)
    }
}
