package unithon.helpjob.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber
import unithon.helpjob.util.ErrorHandler

abstract class BaseViewModel : ViewModel() {

    // 에러 이벤트를 UI로 전달
    private val _errorEvent = MutableSharedFlow<Int>()
    val errorEvent: SharedFlow<Int> = _errorEvent.asSharedFlow()

    // 공통 CoroutineExceptionHandler
    protected val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable, "Coroutine exception occurred")
        val errorMessageResId = ErrorHandler.getErrorMessage(throwable)
        _errorEvent.tryEmit(errorMessageResId)
    }

    // 에러 발생 시 UI에 알림
    protected fun emitError(messageResId: Int) {
        _errorEvent.tryEmit(messageResId)
    }
}