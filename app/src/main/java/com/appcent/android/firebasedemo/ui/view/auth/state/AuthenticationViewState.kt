package com.appcent.android.firebasedemo.ui.view.auth.state

/**
 * Created by hasan.arinc on 4.12.2023.
 */

sealed interface AuthenticationViewState {
    object Loading : AuthenticationViewState
    object Success : AuthenticationViewState
    data class Error(val errorMessage: String) : AuthenticationViewState
}
