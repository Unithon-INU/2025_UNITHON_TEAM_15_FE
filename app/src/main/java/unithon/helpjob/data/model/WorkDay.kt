package unithon.helpjob.data.model

import android.content.Context
import androidx.annotation.StringRes
import unithon.helpjob.R

/**
 * 근무 요일 관리 Enum
 * - UI 표시용 String Resource ID
 * - API 전송용 한글 값
 */
enum class WorkDay(
    @StringRes val displayNameResId: Int,
    val apiValue: String  // API에는 항상 한글로 전송
) {
    MONDAY(
        displayNameResId = R.string.workday_monday,
        apiValue = "월"
    ),
    TUESDAY(
        displayNameResId = R.string.workday_tuesday,
        apiValue = "화"
    ),
    WEDNESDAY(
        displayNameResId = R.string.workday_wednesday,
        apiValue = "수"
    ),
    THURSDAY(
        displayNameResId = R.string.workday_thursday,
        apiValue = "목"
    ),
    FRIDAY(
        displayNameResId = R.string.workday_friday,
        apiValue = "금"
    ),
    SATURDAY(
        displayNameResId = R.string.workday_saturday,
        apiValue = "토"
    ),
    SUNDAY(
        displayNameResId = R.string.workday_sunday,
        apiValue = "일"
    );

    /**
     * Context로 현재 언어에 맞는 표시 이름 반환
     */
    fun getDisplayName(context: Context): String {
        return context.getString(displayNameResId)
    }

    companion object {
        /**
         * API 값(한글)을 기반으로 WorkDay 찾기
         */
        fun fromApiValue(apiValue: String): WorkDay? {
            return entries.find { it.apiValue == apiValue }
        }

        /**
         * UI에서 선택한 텍스트를 기반으로 WorkDay 찾기
         */
        fun fromDisplayText(displayText: String): WorkDay? {
            return when {
                // 한글 버전 매핑
                displayText.contains("월") -> MONDAY
                displayText.contains("화") -> TUESDAY
                displayText.contains("수") -> WEDNESDAY
                displayText.contains("목") -> THURSDAY
                displayText.contains("금") -> FRIDAY
                displayText.contains("토") -> SATURDAY
                displayText.contains("일") -> SUNDAY

                // 영어 버전 매핑
                displayText.contains("Mon") -> MONDAY
                displayText.contains("Tue") -> TUESDAY
                displayText.contains("Wed") -> WEDNESDAY
                displayText.contains("Thu") -> THURSDAY
                displayText.contains("Fri") -> FRIDAY
                displayText.contains("Sat") -> SATURDAY
                displayText.contains("Sun") -> SUNDAY

                else -> null // 매칭되지 않은 경우
            }
        }

        /**
         * WorkDay 목록을 API 값으로 변환 (서버 전송용)
         */
        fun toApiValues(workDays: List<WorkDay>): String {
            return workDays.joinToString(",") { it.apiValue }
        }

        /**
         * API 값 문자열을 WorkDay 목록으로 변환
         */
        fun fromApiValues(apiValues: String): List<WorkDay> {
            if (apiValues.isBlank()) return emptyList()
            return apiValues.split(",")
                .mapNotNull { apiValue -> fromApiValue(apiValue.trim()) }
        }

        /**
         * 첫 번째 줄 요일들 (월~목)
         */
        val firstRowDays = listOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY)

        /**
         * 두 번째 줄 요일들 (금~일)
         */
        val secondRowDays = listOf(FRIDAY, SATURDAY, SUNDAY)
    }
}