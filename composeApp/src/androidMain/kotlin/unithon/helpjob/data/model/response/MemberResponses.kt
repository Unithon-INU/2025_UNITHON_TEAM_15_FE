package unithon.helpjob.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("token")
    val token: String
)

@Serializable
data class MemberProfileGetRes(
    @SerialName("language")
    val language: String,
    @SerialName("visaType")
    val visaType: String,
    @SerialName("topikLevel")
    val topikLevel: String,
    @SerialName("industry")
    val industry: String
)