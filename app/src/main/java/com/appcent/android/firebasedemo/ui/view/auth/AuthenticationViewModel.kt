package com.appcent.android.firebasedemo.ui.view.auth

import androidx.lifecycle.viewModelScope
import com.appcent.android.firebasedemo.domain.data.ApiResult
import com.appcent.android.firebasedemo.domain.repository.AuthRepository
import com.appcent.android.firebasedemo.ui.view.auth.state.AuthenticationViewState
import com.mkhakpaki.sinatobechanged.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by hasan.arinc on 4.12.2023.
 */

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _authenticationViewState = MutableSharedFlow<AuthenticationViewState>()
    val authenticationViewState = _authenticationViewState.asSharedFlow()

    init {
        viewModelScope.launch {
            if (authRepository.currentUser != null) {
                _authenticationViewState.emit(AuthenticationViewState.Success)
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authenticationViewState.emit(AuthenticationViewState.Loading)
            val state = when (val result = authRepository.login(email, password)) {

                is ApiResult.Loading -> {
                    AuthenticationViewState.Loading
                }

                is ApiResult.Success -> {
                    AuthenticationViewState.Success
                }

                is ApiResult.Error -> {
                    AuthenticationViewState.Error(result.error.message.orEmpty())
                }
            }
            _authenticationViewState.emit(state)
        }
    }

    fun singIn(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authenticationViewState.emit(AuthenticationViewState.Loading)
            val state = when (val result = authRepository.signUp(name, email, password)) {

                ApiResult.Loading -> {
                    AuthenticationViewState.Loading
                }

                is ApiResult.Success -> {
                    AuthenticationViewState.Success
                }

                is ApiResult.Error -> {
                    AuthenticationViewState.Error(result.error.message.orEmpty())
                }
            }
            _authenticationViewState.emit(state)
        }
    }
}

