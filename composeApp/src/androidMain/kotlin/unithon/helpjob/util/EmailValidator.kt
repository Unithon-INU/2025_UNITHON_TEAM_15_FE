package unithon.helpjob.util

import android.util.Patterns

/**
 * Android 플랫폼 이메일 유효성 검증 구현
 * Android의 Patterns.EMAIL_ADDRESS 사용
 */
actual object EmailValidator {
    actual fun isValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
