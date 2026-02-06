package unithon.helpjob.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UniversityResponse(
    @SerialName("university")
    val university: String,
    @SerialName("majors")
    val majors: List<MajorInfo>
)

@Serializable
data class MajorInfo(
    @SerialName("major")
    val major: String,
    @SerialName("lssnTerm")
    val lssnTerm: String
)
