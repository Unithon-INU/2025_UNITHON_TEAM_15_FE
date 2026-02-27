package unithon.helpjob.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AccreditedUniversityRes(
    val university: List<UniversityInfo>
)

@Serializable
data class UniversityInfo(
    val nameKo: String,
    val nameEn: String,
    val universityType: String  // "BACHELOR" | "ASSOCIATE" | "GRADUATE"
)
