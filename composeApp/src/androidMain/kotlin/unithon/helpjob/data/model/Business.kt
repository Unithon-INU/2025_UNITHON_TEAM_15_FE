// data/model/Business.kt
package unithon.helpjob.data.model

import android.content.Context
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.desc
import unithon.helpjob.resources.MR

/**
 * 업무 직종 관리 Enum
 * - UI 표시용 String Resource (Moko Resources)
 * - API 전송용 한글 값
 */
enum class Business(
    val displayNameRes: StringResource,
    val apiValue: String  // API에는 항상 한글로 전송
) {
    RESTAURANT(
        displayNameRes = MR.strings.onboarding_business_setup_restaurant,
        apiValue = "음식점/카페"
    ),
    MART(
        displayNameRes = MR.strings.onboarding_business_setup_mart,
        apiValue = "편의점/마트"
    ),
    LOGISTICS(
        displayNameRes = MR.strings.onboarding_business_setup_logistics,
        apiValue = "물류/창고작업"
    ),
    OFFICE(
        displayNameRes = MR.strings.onboarding_business_setup_office,
        apiValue = "사무보조/문서정리"
    ),
    TRANSLATION(
        displayNameRes = MR.strings.onboarding_business_setup_translation,
        apiValue = "통역/번역"
    ),
    TUTORING(
        displayNameRes = MR.strings.onboarding_business_setup_learn,
        apiValue = "과외/학습보조"
    ),
    EVENT(
        displayNameRes = MR.strings.onboarding_business_setup_event,
        apiValue = "행사/이벤트스태프"
    );

    /**
     * Context로 현재 언어에 맞는 표시 이름 반환
     */
    fun getDisplayName(context: Context): String {
        return displayNameRes.desc().toString(context)
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