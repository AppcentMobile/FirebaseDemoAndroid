package com.appcent.android.firebasedemo.ui.view.auth

import androidx.lifecycle.viewModelScope
import com.appcent.android.firebasedemo.domain.data.ApiResult
import com.appcent.android.firebasedemo.domain.repository.AuthRepository
import com.appcent.android.firebasedemo.ui.view.auth.state.LoginViewState
import com.mkhakpaki.sinatobechanged.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by hasan.arinc on 4.12.2023.
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _loginViewState =
        MutableStateFlow<LoginViewState>(LoginViewState.Loading)
    val loginViewState = _loginViewState.asStateFlow()


    init {
        login()
    }

    fun checkCurrentUser() {
        viewModelScope.launch {
            authRepository.currentUser
        }
    }

    private fun login() {
        viewModelScope.launch {
            val result = authRepository.signUp("hasanTest", "test1@gmail.com", "123456")
            val state = when (result) {
                is ApiResult.Error -> {
                    LoginViewState.Error(result.error.message.orEmpty())
                }

                is ApiResult.Loading -> {
                    LoginViewState.Loading
                }

                is ApiResult.Success -> {
                    LoginViewState.Success(result.data)

                }
            }
            _loginViewState.emit(state)
        }
    }
}

