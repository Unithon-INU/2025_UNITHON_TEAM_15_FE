package unithon.helpjob.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateChecklistRequest(
    @SerialName("memberCheckStep")
    val memberCheckStep: String,
    @SerialName("updateEmplCheckDetailReqs")
    val updateEmplCheckDetailReqs: List<UpdateEmplCheckDetailReq>
)

@Serializable
data class UpdateEmplCheckDetailReq(
    @SerialName("checkStep")
    val checkStep: String,
    @SerialName("checked")
    val checked: Boolean,
    @SerialName("isChecked")
    val isChecked: Boolean,
    @SerialName("submissionIdx")
    val submissionIdx: Int
)

enum class Steps(val apiStep: String, val uiStep: String) {
    STEP1("STEP1", "Step1"),
    STEP2("STEP2", "Step2"),
    STEP3("STEP3", "Step3");

    override fun toString(): String = apiStep
}