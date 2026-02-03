package unithon.helpjob.data.model

/**
 * 프로필 수정 가능 필드 - API 경로에 직접 사용
 * PATCH /api/member/me/profile/{profileField}
 */
enum class ProfileField {
    VISA_TYPE,
    LANGUAGE_LEVEL,
    INDUSTRY
}
