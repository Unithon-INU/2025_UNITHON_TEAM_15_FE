package unithon.helpjob.data.model

import androidx.compose.runtime.Composable
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.workday_friday
import helpjob.composeapp.generated.resources.workday_monday
import helpjob.composeapp.generated.resources.workday_saturday
import helpjob.composeapp.generated.resources.workday_sunday
import helpjob.composeapp.generated.resources.workday_thursday
import helpjob.composeapp.generated.resources.workday_tuesday
import helpjob.composeapp.generated.resources.workday_wednesday
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * 근무 요일 관리 Enum
 * - UI 표시용 String Resource (Compose Multiplatform Resources)
 * - API 전송용 언어별 값
 */
enum class WorkDay(
    val displayNameRes: StringResource,
    val apiValueKo: String,
    val apiValueEn: String
) {
    MONDAY(
        displayNameRes = Res.string.workday_monday,
        apiValueKo = "월",
        apiValueEn = "Mon"
    ),
    TUESDAY(
        displayNameRes = Res.string.workday_tuesday,
        apiValueKo = "화",
        apiValueEn = "Tue"
    ),
    WEDNESDAY(
        displayNameRes = Res.string.workday_wednesday,
        apiValueKo = "수",
        apiValueEn = "Wed"
    ),
    THURSDAY(
        displayNameRes = Res.string.workday_thursday,
        apiValueKo = "목",
        apiValueEn = "Thu"
    ),
    FRIDAY(
        displayNameRes = Res.string.workday_friday,
        apiValueKo = "금",
        apiValueEn = "Fri"
    ),
    SATURDAY(
        displayNameRes = Res.string.workday_saturday,
        apiValueKo = "토",
        apiValueEn = "Sat"
    ),
    SUNDAY(
        displayNameRes = Res.string.workday_sunday,
        apiValueKo = "일",
        apiValueEn = "Sun"
    );

    fun apiValue(language: AppLanguage): String = when (language) {
        AppLanguage.KOREAN -> apiValueKo
        AppLanguage.ENGLISH -> apiValueEn
    }

    /**
     * Composable에서 현재 언어에 맞는 표시 이름 반환
     */
    @Composable
    fun getDisplayName(): String {
        return stringResource(displayNameRes)
    }
}
