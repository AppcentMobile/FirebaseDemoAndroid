package com.appcent.android.firebasedemo.domain.repository

import android.content.res.Resources.NotFoundException
import com.appcent.android.firebasedemo.domain.data.ApiResult
import com.appcent.android.firebasedemo.domain.util.extensions.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

/**
 * Created by hasan.arinc on 4.12.2023.
 */

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): ApiResult<FirebaseUser> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user
            if (user != null) {
                ApiResult.Success(user)
            } else {
                ApiResult.Error(NotFoundException())
            }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): ApiResult<FirebaseUser> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            val user = authResult.user
            if (user != null) {
                ApiResult.Success(user)
            } else {
                ApiResult.Error(NotFoundException())
            }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}

