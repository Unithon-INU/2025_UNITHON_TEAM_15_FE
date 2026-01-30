package unithon.helpjob.data.model

import androidx.compose.runtime.Composable
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.onboarding_english_level_cefr
import helpjob.composeapp.generated.resources.onboarding_english_level_ielts
import helpjob.composeapp.generated.resources.onboarding_english_level_none
import helpjob.composeapp.generated.resources.onboarding_english_level_teps
import helpjob.composeapp.generated.resources.onboarding_english_level_toefl
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * 영어 능력 레벨 관리 Enum
 * - UI 표시용 String Resource (Compose Multiplatform Resources)
 * - API 전송용 한글 값
 */
enum class EnglishLevel(
    val displayNameRes: StringResource,
    val apiValue: String
) {
    TOEFL_530(
        displayNameRes = Res.string.onboarding_english_level_toefl,
        apiValue = "TOEFL 530(CBT 197, iBT 71) 이상"
    ),
    IELTS_5_5(
        displayNameRes = Res.string.onboarding_english_level_ielts,
        apiValue = "IELTS 5.5 이상"
    ),
    CEFR_B2(
        displayNameRes = Res.string.onboarding_english_level_cefr,
        apiValue = "CEFR B2 이상"
    ),
    TEPS_600(
        displayNameRes = Res.string.onboarding_english_level_teps,
        apiValue = "TEPS 600점 이상"
    ),
    NO_CERTIFICATION(
        displayNameRes = Res.string.onboarding_english_level_none,
        apiValue = "자격증 없음"
    );

    @Composable
    fun getDisplayName(): String {
        return stringResource(displayNameRes)
    }

    companion object {
        /**
         * UI에서 선택한 텍스트를 기반으로 EnglishLevel 찾기
         */
        fun fromDisplayText(displayText: String): EnglishLevel? {
            return when {
                displayText.contains("TOEFL") -> TOEFL_530
                displayText.contains("IELTS") -> IELTS_5_5
                displayText.contains("CEFR") -> CEFR_B2
                displayText.contains("TEPS") -> TEPS_600
                displayText.contains("자격증 없음") || displayText.contains("No certification") -> NO_CERTIFICATION
                else -> null
            }
        }

        /**
         * API 응답 값으로부터 EnglishLevel 찾기
         */
        fun fromApiValue(apiValue: String): EnglishLevel? {
            return entries.find { it.apiValue == apiValue }
        }
    }
}
