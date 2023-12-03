package com.mkhakpaki.sinatobechanged.ui.base
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

open class BaseViewModel : ViewModel() {

    private val job = Job()

    // avoids app crash when there is no Internet connection or server connection error
    private val exceptionHandler = CoroutineExceptionHandler{ _, throwable->
        throwable.printStackTrace()
    }

    val coroutineScope = CoroutineScope(Dispatchers.IO + job + exceptionHandler)

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

}