package unithon.helpjob.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {
    // 크래시 방지용 (최후의 방어선)
    protected val crashPreventionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable, "Unhandled exception - this should have been caught!")
    }
}