package unithon.helpjob.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateEmploymentCheckResponse(
    @SerialName("progress")
    val progress: Int
)

@Serializable
data class HomeInfoResponse(
    @SerialName("employmentCheckRes")
    val employmentCheckRes: List<EmploymentCheckRes>,
    @SerialName("memberCheckStep")
    val memberCheckStep: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("progress")
    val progress: Int
)

@Serializable
data class EmploymentCheckRes(
    @SerialName("checkStep")
    val checkStep: String,
    @SerialName("documentInfoRes")
    val documentInfoRes: List<DocumentInfoRes>,
    @SerialName("stepInfoRes")
    val stepInfoRes: StepInfoRes
)

@Serializable
data class DocumentInfoRes(
    @SerialName("isChecked")
    val isChecked: Boolean,
    @SerialName("submissionIdx")
    val submissionIdx: Int,
    @SerialName("title")
    val title: String
)

@Serializable
data class StepInfoRes(
    @SerialName("precautions")
    val precautions: List<String>,
    @SerialName("subtitle")
    val subtitle: String,
    @SerialName("title")
    val title: String
)

@Serializable
class TipResponse : ArrayList<TipResponseItem>()

@Serializable
data class TipResponseItem(
    @SerialName("tipInfoDetailRes")
    val tipInfoDetailRes: List<TipInfoDetailRes>,
    @SerialName("title")
    val title: String
)

@Serializable
data class TipInfoDetailRes(
    @SerialName("itemContent")
    val itemContent: String? = null,
    @SerialName("itemTitle")
    val itemTitle: String? = null,
    @SerialName("warning")
    val warning: String? = null
)
