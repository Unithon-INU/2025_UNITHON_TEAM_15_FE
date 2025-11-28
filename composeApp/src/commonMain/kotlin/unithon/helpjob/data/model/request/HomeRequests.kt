package unithon.helpjob.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateEmploymentCheckRequest(
    @SerialName("checkStep")
    val checkStep: String,
    @SerialName("submissionIdx")
    val submissionIdx: Int,
)

enum class Steps(val apiStep: String, val uiStep: String) {
    STEP1("STEP1", "Step1"),
    STEP2("STEP2", "Step2"),
    STEP3("STEP3", "Step3"),
    STEP4("STEP4", "Step4");

    override fun toString(): String = apiStep
}
