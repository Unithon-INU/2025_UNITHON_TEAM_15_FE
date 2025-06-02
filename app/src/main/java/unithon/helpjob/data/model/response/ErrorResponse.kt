package unithon.helpjob.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("code")
    val code: String,
    @SerialName("msg")
    val messages: List<String>
) {
    val message: String
        get() = messages.firstOrNull() ?: "알 수 없는 오류가 발생했습니다."
}