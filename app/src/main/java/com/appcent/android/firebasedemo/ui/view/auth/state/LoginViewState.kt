package com.appcent.android.firebasedemo.ui.view.auth.state

import com.google.firebase.auth.FirebaseUser

/**
 * Created by hasan.arinc on 4.12.2023.
 */

sealed class LoginViewState {
    object Loading : LoginViewState()
    data class Success(val data: FirebaseUser) : LoginViewState()
    data class Error(val errorMessage: String) : LoginViewState()
}
