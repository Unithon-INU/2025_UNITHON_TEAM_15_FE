// data/model/Business.kt
package unithon.helpjob.data.model

import androidx.compose.runtime.Composable
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.onboarding_business_setup_event
import helpjob.composeapp.generated.resources.onboarding_business_setup_learn
import helpjob.composeapp.generated.resources.onboarding_business_setup_logistics
import helpjob.composeapp.generated.resources.onboarding_business_setup_mart
import helpjob.composeapp.generated.resources.onboarding_business_setup_office
import helpjob.composeapp.generated.resources.onboarding_business_setup_restaurant
import helpjob.composeapp.generated.resources.onboarding_business_setup_translation
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * 업무 직종 관리 Enum
 * - UI 표시용 String Resource (Compose Multiplatform Resources)
 * - API 전송용 언어별 값
 */
enum class Business(
    val displayNameRes: StringResource,
    val apiValueKo: String,
    val apiValueEn: String
) {
    RESTAURANT(
        displayNameRes = Res.string.onboarding_business_setup_restaurant,
        apiValueKo = "음식점/카페",
        apiValueEn = "Restaurant/Cafe"
    ),
    MART(
        displayNameRes = Res.string.onboarding_business_setup_mart,
        apiValueKo = "편의점/마트",
        apiValueEn = "Convenience Store/Mart"
    ),
    LOGISTICS(
        displayNameRes = Res.string.onboarding_business_setup_logistics,
        apiValueKo = "물류/창고작업",
        apiValueEn = "Logistics / Warehouse Assistant"
    ),
    OFFICE(
        displayNameRes = Res.string.onboarding_business_setup_office,
        apiValueKo = "사무활동보조",
        apiValueEn = "Office Assistant"
    ),
    TRANSLATION(
        displayNameRes = Res.string.onboarding_business_setup_translation,
        apiValueKo = "통역활동보조",
        apiValueEn = "Interpretation Assistant"
    ),
    TUTORING(
        displayNameRes = Res.string.onboarding_business_setup_learn,
        apiValueKo = "학습활동보조",
        apiValueEn = "Learning Assistant"
    ),
    EVENT(
        displayNameRes = Res.string.onboarding_business_setup_event,
        apiValueKo = "행사/이벤트스태프",
        apiValueEn = "Event Staff / Promotional Support"
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

    companion object {
        /**
         * API 값(한글)을 기반으로 Business 찾기
         */
        fun fromApiValue(apiValue: String): Business? {
            return entries.find { it.apiValueKo == apiValue }
        }

        /**
         * UI에서 선택한 텍스트를 기반으로 Business 찾기
         */
        fun fromDisplayText(displayText: String): Business? {
            return when {
                // 한글 버전 매핑
                displayText.contains("음식점") || displayText.contains("카페") -> RESTAURANT
                displayText.contains("편의점") || displayText.contains("마트") -> MART
                displayText.contains("물류") -> LOGISTICS
                displayText.contains("사무") -> OFFICE
                displayText.contains("번역") || displayText.contains("통역") -> TRANSLATION
                displayText.contains("학습") -> TUTORING
                displayText.contains("이벤트") || displayText.contains("단기") -> EVENT

                // 영어 버전 매핑
                displayText.contains("Restaurant") -> RESTAURANT
                displayText.contains("Mart") || displayText.contains("Retail") -> MART
                displayText.contains("Logistics") -> LOGISTICS
                displayText.contains("Office") -> OFFICE
                displayText.contains("Translation") || displayText.contains("Interpretation") -> TRANSLATION
                displayText.contains("Learning") -> TUTORING
                displayText.contains("Event") || displayText.contains("Part-time") -> EVENT

                else -> null // 매칭되지 않은 경우
            }
        }

        /**
         * 모든 Business 목록을 API 값으로 변환
         */
        fun toApiValues(businesses: List<Business>): String {
            return businesses.joinToString(",") { it.apiValueKo }
        }
    }
}