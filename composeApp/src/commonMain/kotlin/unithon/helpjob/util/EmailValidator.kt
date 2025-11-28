package unithon.helpjob.util

/**
 * 플랫폼별 이메일 유효성 검증 유틸리티
 */
expect object EmailValidator {
    /**
     * 이메일 주소 형식이 유효한지 검증
     * @param email 검증할 이메일 주소
     * @return 유효한 형식이면 true, 아니면 false
     */
    fun isValid(email: String): Boolean
}
