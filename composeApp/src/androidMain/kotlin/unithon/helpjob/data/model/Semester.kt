package unithon.helpjob.data.model

import android.content.Context
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.desc
import unithon.helpjob.resources.MR

/**
 * 학기 관리 Enum
 * - UI 표시용 String Resource (Moko Resources)
 * - API 전송용 한글 값
 */
enum class Semester(
    val displayNameRes: StringResource,
    val apiValue: String  // API에는 항상 한글로 전송
) {
    FIRST_YEAR_FIRST(
        displayNameRes = MR.strings.semester_1_1,
        apiValue = "1학기"
    ),
    FIRST_YEAR_SECOND(
        displayNameRes = MR.strings.semester_1_2,
        apiValue = "2학기"
    ),
    SECOND_YEAR_FIRST(
        displayNameRes = MR.strings.semester_2_1,
        apiValue = "3학기"
    ),
    SECOND_YEAR_SECOND(
        displayNameRes = MR.strings.semester_2_2,
        apiValue = "4학기"
    ),
    THIRD_YEAR_FIRST(
        displayNameRes = MR.strings.semester_3_1,
        apiValue = "5학기"
    ),
    THIRD_YEAR_SECOND(
        displayNameRes = MR.strings.semester_3_2,
        apiValue = "6학기"
    ),
    FOURTH_YEAR_FIRST(
        displayNameRes = MR.strings.semester_4_1,
        apiValue = "7학기"
    ),
    FOURTH_YEAR_SECOND(
        displayNameRes = MR.strings.semester_4_2,
        apiValue = "8학기"
    );

    /**
     * Context로 현재 언어에 맞는 표시 이름 반환
     */
    fun getDisplayName(context: Context): String {
        return displayNameRes.desc().toString(context)
    }

    companion object {
        /**
         * API 값(한글)을 기반으로 Semester 찾기
         */
        fun fromApiValue(apiValue: String): Semester? {
            return entries.find { it.apiValue == apiValue }
        }

        /**
         * UI에서 선택한 텍스트를 기반으로 Semester 찾기
         */
        fun fromDisplayText(displayText: String): Semester? {
            return when {
                // 한글 버전 매핑
                displayText.contains("1학년 1학기") -> FIRST_YEAR_FIRST
                displayText.contains("1학년 2학기") -> FIRST_YEAR_SECOND
                displayText.contains("2학년 1학기") -> SECOND_YEAR_FIRST
                displayText.contains("2학년 2학기") -> SECOND_YEAR_SECOND
                displayText.contains("3학년 1학기") -> THIRD_YEAR_FIRST
                displayText.contains("3학년 2학기") -> THIRD_YEAR_SECOND
                displayText.contains("4학년 1학기") -> FOURTH_YEAR_FIRST
                displayText.contains("4학년 2학기") -> FOURTH_YEAR_SECOND

                // 영어 버전 매핑
                displayText.contains("1st Year 1st Semester") -> FIRST_YEAR_FIRST
                displayText.contains("1st Year 2nd Semester") -> FIRST_YEAR_SECOND
                displayText.contains("2nd Year 1st Semester") -> SECOND_YEAR_FIRST
                displayText.contains("2nd Year 2nd Semester") -> SECOND_YEAR_SECOND
                displayText.contains("3rd Year 1st Semester") -> THIRD_YEAR_FIRST
                displayText.contains("3rd Year 2nd Semester") -> THIRD_YEAR_SECOND
                displayText.contains("4th Year 1st Semester") -> FOURTH_YEAR_FIRST
                displayText.contains("4th Year 2nd Semester") -> FOURTH_YEAR_SECOND

                else -> null // 매칭되지 않은 경우
            }
        }
    }
}