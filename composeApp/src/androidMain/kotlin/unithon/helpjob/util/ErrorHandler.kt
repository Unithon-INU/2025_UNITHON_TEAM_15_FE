package unithon.helpjob.util

import timber.log.Timber
import unithon.helpjob.R
import unithon.helpjob.data.repository.*

object ErrorHandler {

    fun getErrorMessage(throwable: Throwable): Int {
        Timber.e(throwable, "Error occurred: ${throwable.message}")

        return when (throwable) {
            // 로그인 관련
            is EmailNotFoundException -> R.string.sign_in_email_not_found
            is WrongPasswordException -> R.string.sign_in_password_wrong

            // 이메일 인증 관련
            is EmailAlreadyInUseException -> R.string.sign_up_email_exists
            is EmailCodeExpiredException -> R.string.verification_code_expired
            is EmailVerificationFailedException -> R.string.verification_code_invalid

            // 닉네임 관련
            is NicknameDuplicateException -> R.string.nickname_duplicate_error

            // 인증 관련
            is UnauthorizedException -> R.string.error_authentication_required

            // 기본 에러
            else -> R.string.error_unknown
        }
    }
}