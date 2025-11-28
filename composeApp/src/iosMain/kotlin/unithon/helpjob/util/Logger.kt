package unithon.helpjob.util

/**
 * iOS 플랫폼 로거 구현
 * println을 사용한 로깅
 */
actual object Logger {
    actual fun d(message: String) {
        println("[DEBUG] $message")
    }

    actual fun d(tag: String, message: String) {
        println("[DEBUG] [$tag] $message")
    }

    actual fun e(message: String) {
        println("[ERROR] $message")
    }

    actual fun e(tag: String, message: String) {
        println("[ERROR] [$tag] $message")
    }

    actual fun e(throwable: Throwable, message: String) {
        println("[ERROR] $message")
        println(throwable.stackTraceToString())
    }

    actual fun i(message: String) {
        println("[INFO] $message")
    }

    actual fun i(tag: String, message: String) {
        println("[INFO] [$tag] $message")
    }
}
