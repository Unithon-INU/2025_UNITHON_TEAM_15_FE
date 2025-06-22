package unithon.helpjob.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentRequest(
    @SerialName("address")
    val address: String,
    @SerialName("bizRegNum")
    val bizRegNum: String,
    @SerialName("companyName")
    val companyName: String,
    @SerialName("companyPhoneNum")
    val companyPhoneNum: String,
    @SerialName("email")
    val email: String,
    @SerialName("hourlyWage")
    val hourlyWage: String,
    @SerialName("industry")
    val industry: String,
    @SerialName("major")
    val major: String,
    @SerialName("name")
    val name: String,
    @SerialName("phoneNum")
    val phoneNum: String,
    @SerialName("regNum")
    val regNum: String,
    @SerialName("semester")
    val semester: String,
    @SerialName("weekdayWorkTimes")
    val weekdayWorkTimes: List<WeekdayWorkTime>,
    @SerialName("weekendWorkTimes")
    val weekendWorkTimes: List<WeekendWorkTime>,
    @SerialName("workingEndDate")
    val workingEndDate: String,
    @SerialName("workingStartDate")
    val workingStartDate: String
)

@Serializable
data class WeekdayWorkTime(
    @SerialName("day")
    val day: List<String>,
    @SerialName("workingEndTime")
    val workingEndTime: String,
    @SerialName("workingStartTime")
    val workingStartTime: String
)

@Serializable
data class WeekendWorkTime(
    @SerialName("day")
    val day: List<String>,
    @SerialName("workingEndTime")
    val workingEndTime: String,
    @SerialName("workingStartTime")
    val workingStartTime: String
)