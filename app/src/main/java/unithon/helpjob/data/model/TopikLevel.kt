import androidx.annotation.StringRes
import unithon.helpjob.R

/**
 * TOPIK 레벨 관리 Enum
 * - UI 표시용 String Resource ID
 * - API 전송용 한글 값
 */
enum class TopikLevel(
    @StringRes val displayNameResId: Int,
    val apiValue: String  // API에는 항상 한글로 전송
) {
    NO_TOPIK(
        displayNameResId = R.string.onboarding_korean_level_setup_no_topik,
        apiValue = "없음"
    ),
    TOPIK_3_OR_BELOW(
        displayNameResId = R.string.onboarding_korean_level_setup_topik3,
        apiValue = "TOPIK 3급"
    ),
    TOPIK_4_OR_ABOVE(
        displayNameResId = R.string.onboarding_korean_level_setup_topik4_over,
        apiValue = "TOPIK 4급 이상"
    );

    companion object {
        /**
         * UI에서 선택한 텍스트를 기반으로 TopikLevel 찾기
         */
        fun fromDisplayText(displayText: String): TopikLevel {
            return when {
                displayText.contains("TOPIK 3급") || displayText.contains("Level 3") -> TOPIK_3_OR_BELOW

                displayText.contains("TOPIK 4급") || displayText.contains("Level 4") -> TOPIK_4_OR_ABOVE

                displayText.contains("TOPIK 없음") || displayText.contains("No TOPIK") -> NO_TOPIK

                else -> NO_TOPIK // 기본값
            }
        }
    }
}