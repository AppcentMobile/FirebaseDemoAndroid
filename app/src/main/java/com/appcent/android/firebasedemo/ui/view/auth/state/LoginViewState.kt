package com.appcent.android.firebasedemo.ui.view.auth.state

import com.google.firebase.auth.FirebaseUser

/**
 * Created by hasan.arinc on 4.12.2023.
 */

sealed interface LoginViewState {
    object Loading : LoginViewState
    object Success : LoginViewState
    data class Error(val errorMessage: String) : LoginViewState
}
