package com.appcent.android.firebasedemo.di

import com.appcent.android.firebasedemo.domain.FirebaseDBHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by hasan.arinc on 4.12.2023.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideDBHelper(databaseReference: DatabaseReference, firebaseAuth: FirebaseAuth) =
        FirebaseDBHelper(databaseReference, firebaseAuth)

    @Provides
    @Singleton
    fun provideDataBaseReference(): DatabaseReference = FirebaseDatabase.getInstance().reference

}