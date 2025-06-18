package unithon.helpjob.data.network

object ApiConstants {
    // BASE URL은 BuildConfig에서 관리

    // member-controller Endpoint
    const val SIGN_IN = "api/member/sign-in"
    const val SIGN_UP = "api/member/sign-up"
    const val SET_NICKNAME = "api/member/me/nickname"
    const val SET_PROFILE = "api/member/me/profile"
    const val GET_PROFILE = "api/member/me/profile"
    const val EMAIL_SEND = "api/email/send"
    const val EMAIL_VERIFY = "api/email/verify"
    const val UPDATE_CHECKLIST = "api/employment"
    const val GET_HOME_INFO = "api/employment/home"
    const val GET_TIPS = "api/employment/tips"
}