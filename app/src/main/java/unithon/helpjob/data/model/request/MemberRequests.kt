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
data class MemberProfileReq(
    @SerialName("language")
    val language: String,
    @SerialName("languageLevel")
    val languageLevel: String,
    @SerialName("visaType")
    val visaType: String,
    @SerialName("industry")
    val industry: String
)