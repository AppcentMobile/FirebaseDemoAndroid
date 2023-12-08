package com.appcent.android.firebasedemo.domain.repository

import com.appcent.android.firebasedemo.domain.data.ApiResult
import com.google.firebase.auth.FirebaseUser

/**
 * Created by hasan.arinc on 4.12.2023.
 */

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): ApiResult<FirebaseUser>
    suspend fun signUp(name: String, email: String, password: String): ApiResult<FirebaseUser>
    fun logout()
}