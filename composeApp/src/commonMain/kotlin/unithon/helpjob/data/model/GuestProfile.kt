package unithon.helpjob.data.model

import kotlinx.serialization.Serializable

/**
 * Guest Mode에서 사용되는 프로필 데이터
 * DataStore에 JSON 문자열로 저장됨
 */
@Serializable
data class GuestProfile(
    val language: String,          // "한국어", "English"
    val languageLevel: String,      // TopikLevel/EnglishLevel의 apiValue
    val visaType: String,          // "D-2", "D-4" 등
    val industry: String           // "편의점,카페" (쉼표 구분, Business.toApiValues() 결과)
)

/**
 * Guest Mode에서 사용되는 체크리스트 데이터
 * DataStore에 JSON 문자열로 저장됨
 */
@Serializable
data class GuestChecklist(
    val checkedItems: Map<String, List<Int>> = emptyMap()  // "STEP1" -> [1, 3, 5]
)
