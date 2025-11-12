package unithon.helpjob.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class BaseViewModel : ViewModel() {
    // 크래시 방지용 (최후의 방어선)
    protected val crashPreventionHandler = CoroutineExceptionHandler { _, throwable ->
        // Timber는 Android 전용 - commonMain에서는 println 사용
        println("⚠️ Unhandled exception in ${this::class.simpleName}: ${throwable.message}")
        throwable.printStackTrace()
    }
}
