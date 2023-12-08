package com.appcent.android.firebasedemo.domain.data


/**
 * Created by hasan.arinc on 4.12.2023.
 */

sealed class ApiResult<out R> {
    object Loading : ApiResult<Nothing>()
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val error: Exception) : ApiResult<Nothing>()
}
