package unithon.helpjob.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import unithon.helpjob.util.Logger

abstract class BaseViewModel : ViewModel() {
    // 크래시 방지용 (최후의 방어선)
    protected val crashPreventionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.e("⚠️ Unhandled exception in ${this::class.simpleName}: ${throwable.message}")
        throwable.printStackTrace()
    }
}
