package unithon.helpjob.util

import timber.log.Timber

/**
 * Android 플랫폼 로거 구현
 * Timber를 사용한 로깅
 */
actual object Logger {
    actual fun d(message: String) {
        Timber.d(message)
    }

    actual fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    actual fun e(message: String) {
        Timber.e(message)
    }

    actual fun e(tag: String, message: String) {
        Timber.tag(tag).e(message)
    }

    actual fun e(throwable: Throwable, message: String) {
        Timber.e(throwable, message)
    }

    actual fun i(message: String) {
        Timber.i(message)
    }

    actual fun i(tag: String, message: String) {
        Timber.tag(tag).i(message)
    }
}
