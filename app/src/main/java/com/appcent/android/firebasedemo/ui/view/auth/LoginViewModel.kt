package com.appcent.android.firebasedemo.ui.view.auth

import androidx.lifecycle.viewModelScope
import com.appcent.android.firebasedemo.domain.data.ApiResult
import com.appcent.android.firebasedemo.domain.repository.AuthRepository
import com.appcent.android.firebasedemo.ui.view.auth.state.LoginViewState
import com.mkhakpaki.sinatobechanged.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by hasan.arinc on 4.12.2023.
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _loginViewState = MutableSharedFlow<LoginViewState>()
    val loginViewState = _loginViewState.asSharedFlow()

    init {
        viewModelScope.launch {
            if (authRepository.currentUser != null) {
                _loginViewState.emit(LoginViewState.Success)
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val state = when (val result = authRepository.login(email, password)) {
                is ApiResult.Error -> {
                    LoginViewState.Error(result.error.message.orEmpty())
                }

                is ApiResult.Loading -> {
                    LoginViewState.Loading
                }

                is ApiResult.Success -> {
                    LoginViewState.Success
                }
            }
            _loginViewState.emit(state)
        }
    }
}

