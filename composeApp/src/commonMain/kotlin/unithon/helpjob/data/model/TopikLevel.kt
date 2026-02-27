package unithon.helpjob.data.model

import androidx.compose.runtime.Composable
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.onboarding_korean_level_setup_no_topik
import helpjob.composeapp.generated.resources.onboarding_korean_level_setup_topik2
import helpjob.composeapp.generated.resources.onboarding_korean_level_setup_topik3
import helpjob.composeapp.generated.resources.onboarding_korean_level_setup_topik4_over
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * TOPIK 레벨 관리 Enum
 * - UI 표시용 String Resource (Compose Multiplatform Resources)
 * - API 전송용 한글 값
 */
enum class TopikLevel(
    val displayNameRes: StringResource,
    val apiValue: String  // API에는 항상 한글로 전송
) {
    NO_TOPIK(
        displayNameRes = Res.string.onboarding_korean_level_setup_no_topik,
        apiValue = "없음"
    ),
    TOPIK_2(
        displayNameRes = Res.string.onboarding_korean_level_setup_topik2,
        apiValue = "TOPIK 2급"
    ),
    TOPIK_3_OR_BELOW(
        displayNameRes = Res.string.onboarding_korean_level_setup_topik3,
        apiValue = "TOPIK 3급"
    ),
    TOPIK_4_OR_ABOVE(
        displayNameRes = Res.string.onboarding_korean_level_setup_topik4_over,
        apiValue = "TOPIK 4급 이상"
    );

    /**
     * Composable에서 현재 언어에 맞는 표시 이름 반환
     */
    @Composable
    fun getDisplayName(): String {
        return stringResource(displayNameRes)
    }

    companion object {
        /**
         * UI에서 선택한 텍스트를 기반으로 TopikLevel 찾기
         */
        fun fromDisplayText(displayText: String): TopikLevel {
            return when {
                displayText.contains("TOPIK 2급") || displayText.contains("Level 2") -> TOPIK_2

                displayText.contains("TOPIK 3급") || displayText.contains("Level 3") -> TOPIK_3_OR_BELOW

                displayText.contains("TOPIK 4급") || displayText.contains("Level 4") -> TOPIK_4_OR_ABOVE

                displayText.contains("TOPIK 없음") || displayText.contains("No TOPIK") -> NO_TOPIK

                else -> NO_TOPIK // 기본값
            }
        }
    }
}