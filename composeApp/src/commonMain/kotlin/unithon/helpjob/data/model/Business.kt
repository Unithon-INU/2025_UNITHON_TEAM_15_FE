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
 * - API 전송용 한글 값
 */
enum class Business(
    val displayNameRes: StringResource,
    val apiValue: String  // API에는 항상 한글로 전송
) {
    RESTAURANT(
        displayNameRes = Res.string.onboarding_business_setup_restaurant,
        apiValue = "음식점/카페"
    ),
    MART(
        displayNameRes = Res.string.onboarding_business_setup_mart,
        apiValue = "편의점/마트"
    ),
    LOGISTICS(
        displayNameRes = Res.string.onboarding_business_setup_logistics,
        apiValue = "물류/창고작업"
    ),
    OFFICE(
        displayNameRes = Res.string.onboarding_business_setup_office,
        apiValue = "사무보조/문서정리"
    ),
    TRANSLATION(
        displayNameRes = Res.string.onboarding_business_setup_translation,
        apiValue = "통역/번역"
    ),
    TUTORING(
        displayNameRes = Res.string.onboarding_business_setup_learn,
        apiValue = "과외/학습보조"
    ),
    EVENT(
        displayNameRes = Res.string.onboarding_business_setup_event,
        apiValue = "행사/이벤트스태프"
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
         * API 값(한글)을 기반으로 Business 찾기
         */
        fun fromApiValue(apiValue: String): Business? {
            return entries.find { it.apiValue == apiValue }
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
                displayText.contains("번역") -> TRANSLATION
                displayText.contains("과외") || displayText.contains("학습") -> TUTORING
                displayText.contains("이벤트") || displayText.contains("단기") -> EVENT

                // 영어 버전 매핑
                displayText.contains("Restaurant") -> RESTAURANT
                displayText.contains("Mart") || displayText.contains("Retail") -> MART
                displayText.contains("Logistics") -> LOGISTICS
                displayText.contains("Office") -> OFFICE
                displayText.contains("Translation") -> TRANSLATION
                displayText.contains("Tutoring") || displayText.contains("Learn") -> TUTORING
                displayText.contains("Event") || displayText.contains("Part-time") -> EVENT

                else -> null // 매칭되지 않은 경우
            }
        }

        /**
         * 모든 Business 목록을 API 값으로 변환
         */
        fun toApiValues(businesses: List<Business>): String {
            return businesses.joinToString(",") { it.apiValue }
        }
    }
}