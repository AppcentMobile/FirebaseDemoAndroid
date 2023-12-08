package com.appcent.android.firebasedemo.di

import com.appcent.android.firebasedemo.domain.repository.AuthRepository
import com.appcent.android.firebasedemo.domain.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by hasan.arinc on 4.12.2023.
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}