package unithon.helpjob.util

/**
 * 플랫폼 독립적인 로거 인터페이스
 * Android: Timber 사용
 * iOS: println 사용
 */
expect object Logger {
    fun d(message: String)
    fun d(tag: String, message: String)
    fun e(message: String)
    fun e(tag: String, message: String)
    fun e(throwable: Throwable, message: String)
    fun i(message: String)
    fun i(tag: String, message: String)
}
