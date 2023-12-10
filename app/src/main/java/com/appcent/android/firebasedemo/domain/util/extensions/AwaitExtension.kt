package com.appcent.android.firebasedemo.domain.util.extensions

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

/**
 * Created by hasan.arinc on 4.12.2023.
 */

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Task<T>.await(): T {
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                cont.resume(task.result,null)
            } else {
                cont.resumeWithException(task.exception ?: RuntimeException("Fail"))
            }
        }
    }
}