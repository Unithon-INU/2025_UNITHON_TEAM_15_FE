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
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * 학기 관리 Enum
 * - UI 표시용 String Resource (Compose Multiplatform Resources)
 * - semesterIndex: 1~8 순번 (API 전송값 계산: semesterIndex - 1)
 * - year: 학년 (1~4)
 */
enum class Semester(
    val displayNameRes: StringResource,
    val semesterIndex: Int,  // 1~8 (API 전송: semesterIndex - 1 = 이수학기 수)
    val year: Int
) {
    FIRST_YEAR_FIRST(
        displayNameRes = Res.string.semester_1_1,
        semesterIndex = 1,
        year = 1
    ),
    FIRST_YEAR_SECOND(
        displayNameRes = Res.string.semester_1_2,
        semesterIndex = 2,
        year = 1
    ),
    SECOND_YEAR_FIRST(
        displayNameRes = Res.string.semester_2_1,
        semesterIndex = 3,
        year = 2
    ),
    SECOND_YEAR_SECOND(
        displayNameRes = Res.string.semester_2_2,
        semesterIndex = 4,
        year = 2
    ),
    THIRD_YEAR_FIRST(
        displayNameRes = Res.string.semester_3_1,
        semesterIndex = 5,
        year = 3
    ),
    THIRD_YEAR_SECOND(
        displayNameRes = Res.string.semester_3_2,
        semesterIndex = 6,
        year = 3
    ),
    FOURTH_YEAR_FIRST(
        displayNameRes = Res.string.semester_4_1,
        semesterIndex = 7,
        year = 4
    ),
    FOURTH_YEAR_SECOND(
        displayNameRes = Res.string.semester_4_2,
        semesterIndex = 8,
        year = 4
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
    fun toAcademicYear(isAssociate: Boolean, isGraduate: Boolean): String {
        if (isGraduate) return "GRADUATE"
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
         * studyPeriod에서 최대 학년 추출 ("4년" → 4, "2년" → 2)
         */
        fun parseMaxGrade(studyPeriod: String): Int {
            return studyPeriod.filter { it.isDigit() }.toIntOrNull() ?: 4
        }

        /**
         * maxGrade 기준으로 선택 가능한 학기 필터링 (studyPeriod 기반)
         */
        fun filteredByMaxGrade(maxGrade: Int): List<Semester> {
            return entries.filter { it.year in 1..maxGrade }
        }
    }
}
