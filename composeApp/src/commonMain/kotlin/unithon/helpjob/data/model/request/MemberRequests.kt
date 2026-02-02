package unithon.helpjob.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberSignInReq(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)

@Serializable
data class MemberSignUpReq(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)

@Serializable
data class MemberNicknameReq(
    @SerialName("nickname")
    val nickname: String
)

@Serializable
data class MemberProfileSetReq(
    @SerialName("language")
    val language: String,
    @SerialName("visaType")
    val visaType: String,
    @SerialName("languageLevel")
    val languageLevel: String,
    @SerialName("industry")
    val industry: String
)

// 🆕 이메일 인증 관련 요청 모델들
@Serializable
data class EmailSendReq(
    @SerialName("email")
    val email: String
)

@Serializable
data class EmailVerifyCodeReq(
    @SerialName("email")
    val email: String,
    @SerialName("code")
    val code: String
)
