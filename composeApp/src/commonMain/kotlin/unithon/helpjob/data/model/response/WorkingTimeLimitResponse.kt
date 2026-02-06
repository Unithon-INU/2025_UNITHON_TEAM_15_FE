package unithon.helpjob.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkingTimeLimitResponse(
    @SerialName("weeklyHours")
    val weeklyHours: Int? = null,
    @SerialName("weekdayHours")
    val weekdayHours: Int? = null
)
