package unithon.helpjob.data.repository

/**
 * 인메모리 캐시를 사용하는 리포지토리를 위한 공통 인터페이스.
 * 로그아웃 시 캐시를 일괄 초기화하는 데 사용됩니다.
 */
interface CacheableRepository {
    /**
     * 리포지토리의 인메모리 캐시를 초기화합니다.
     * 로그아웃 시 자동으로 호출됩니다.
     */
    fun clearCache()
}
