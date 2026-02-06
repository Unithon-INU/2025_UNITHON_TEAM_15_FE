package unithon.helpjob.data.model

import androidx.compose.runtime.Composable
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.semester_1_1
import helpjob.composeapp.generated.resources.semester_1_2
import helpjob.composeapp.generated.resources.semester_2_1
import helpjob.composeapp.generated.resources.semester_2_2
import helpjob.composeapp.generated.resources.semester_3_1
import helpjob.composeapp.generated.resources.semester_3_2
import helpjob.composeapp.generated.resources.semester_4_1
import helpjob.composeapp.generated.resources.semester_4_2
import helpjob.composeapp.generated.resources.semester_graduate
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * 학기 관리 Enum
 * - UI 표시용 String Resource (Compose Multiplatform Resources)
 * - API 전송용 한글 값
 * - year: 학년 (1~4), GRADUATE는 0
 */
enum class Semester(
    val displayNameRes: StringResource,
    val apiValue: String,  // API에는 항상 한글로 전송
    val year: Int           // 학년 (1~4), GRADUATE = 0
) {
    FIRST_YEAR_FIRST(
        displayNameRes = Res.string.semester_1_1,
        apiValue = "1학기",
        year = 1
    ),
    FIRST_YEAR_SECOND(
        displayNameRes = Res.string.semester_1_2,
        apiValue = "2학기",
        year = 1
    ),
    SECOND_YEAR_FIRST(
        displayNameRes = Res.string.semester_2_1,
        apiValue = "3학기",
        year = 2
    ),
    SECOND_YEAR_SECOND(
        displayNameRes = Res.string.semester_2_2,
        apiValue = "4학기",
        year = 2
    ),
    THIRD_YEAR_FIRST(
        displayNameRes = Res.string.semester_3_1,
        apiValue = "5학기",
        year = 3
    ),
    THIRD_YEAR_SECOND(
        displayNameRes = Res.string.semester_3_2,
        apiValue = "6학기",
        year = 3
    ),
    FOURTH_YEAR_FIRST(
        displayNameRes = Res.string.semester_4_1,
        apiValue = "7학기",
        year = 4
    ),
    FOURTH_YEAR_SECOND(
        displayNameRes = Res.string.semester_4_2,
        apiValue = "8학기",
        year = 4
    ),
    GRADUATE(
        displayNameRes = Res.string.semester_graduate,
        apiValue = "석박사",
        year = 0
    );

    /**
     * Composable에서 현재 언어에 맞는 표시 이름 반환
     */
    @Composable
    fun getDisplayName(): String {
        return stringResource(displayNameRes)
    }

    /**
     * working-time API의 year 파라미터로 변환
     * @param isAssociate 2년제(전문대) 여부 (maxGrade <= 2)
     */
    fun toAcademicYear(isAssociate: Boolean): String {
        if (this == GRADUATE) return "GRADUATE"
        if (isAssociate) return "ASSOCIATE"
        return when (year) {
            1 -> "BACHELOR_1"
            2 -> "BACHELOR_2"
            3 -> "BACHELOR_3"
            4 -> "BACHELOR_4"
            else -> "BACHELOR_4"
        }
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
                displayText.contains("석박사") || displayText.contains("Graduate") -> GRADUATE

                // 영어 버전 매핑
                displayText.contains("1st Year 1st Semester") -> FIRST_YEAR_FIRST
                displayText.contains("1st Year 2nd Semester") -> FIRST_YEAR_SECOND
                displayText.contains("2nd Year 1st Semester") -> SECOND_YEAR_FIRST
                displayText.contains("2nd Year 2nd Semester") -> SECOND_YEAR_SECOND
                displayText.contains("3rd Year 1st Semester") -> THIRD_YEAR_FIRST
                displayText.contains("3rd Year 2nd Semester") -> THIRD_YEAR_SECOND
                displayText.contains("4th Year 1st Semester") -> FOURTH_YEAR_FIRST
                displayText.contains("4th Year 2nd Semester") -> FOURTH_YEAR_SECOND

                else -> null
            }
        }

        /**
         * lssnTerm에서 최대 학년 추출 ("4학년" → 4, "2학년" → 2)
         */
        fun parseMaxGrade(lssnTerm: String): Int {
            return lssnTerm.filter { it.isDigit() }.toIntOrNull() ?: 4
        }

        /**
         * maxGrade 기준으로 선택 가능한 학기 필터링 (항상 GRADUATE 포함)
         */
        fun filteredByMaxGrade(maxGrade: Int): List<Semester> {
            return entries.filter { it.year in 1..maxGrade || it == GRADUATE }
        }
    }
}
