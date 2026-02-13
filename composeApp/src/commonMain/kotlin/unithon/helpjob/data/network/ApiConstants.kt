package unithon.helpjob.data.network

object ApiConstants {
    // BASE URL은 BuildConfig에서 관리

    // API Endpoint
    const val SIGN_IN = "api/member/sign-in"
    const val SIGN_UP = "api/member/sign-up"
    const val SET_NICKNAME = "api/member/me/nickname"
    const val SET_PROFILE = "api/member/me/profile"
    const val GET_PROFILE = "api/member/me/profile"
    const val PATCH_PROFILE = "api/member/me/profile"
    const val EMAIL_SEND = "api/email/send"
    const val EMAIL_VERIFY = "api/email/verify"
    const val UPDATE_CHECKLIST = "api/employment"
    const val GET_HOME_INFO = "api/employment/home"
    const val GET_TIPS = "api/employment/tips"
    const val RESET_PROGRESS = "api/employment/progress"
    const val POST_CERTIFICATION = "api/cert"
    const val SEARCH_UNIVERSITY = "api/universities"
    const val GET_WORKING_TIME_LIMIT = "api/cert/working-time"
    const val PRIVACY_POLICY = "privacy"
    const val TERMS_OF_SERVICE = "terms"

    // 외부 URL
    const val WITHDRAWAL_FORM_URL = "https://docs.google.com/forms/d/e/1FAIpQLScBgUH8_jGwGdGxkzCjsgI_n1-NQ8RtXeR6npjSZCa37u-rWg/viewform"
}