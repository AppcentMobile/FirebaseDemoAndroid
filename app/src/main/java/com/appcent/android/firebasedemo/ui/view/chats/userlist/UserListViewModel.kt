package com.appcent.android.firebasedemo.ui.view.chats.userlist

import androidx.lifecycle.viewModelScope
import com.appcent.android.firebasedemo.domain.FirebaseDBHelper
import com.appcent.android.firebasedemo.ui.view.auth.state.AuthenticationViewState
import com.appcent.android.firebasedemo.ui.view.chats.userlist.state.UserListViewState
import com.mkhakpaki.sinatobechanged.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val firebaseDBHelper: FirebaseDBHelper
) : BaseViewModel() {

    private val _userListViewState = MutableSharedFlow<UserListViewState>()
    val userListviewState = _userListViewState.asSharedFlow()



    fun getUsers(query: String?=null) {
        viewModelScope.launch {
            _userListViewState.emit(UserListViewState.Loading)
            with(Dispatchers.IO) {
                try {
                    val users = firebaseDBHelper.getUsersListForMessaging(query)
                    if (users.isEmpty()) {
                        _userListViewState.emit(UserListViewState.Empty)
                        return@launch
                    }

                    _userListViewState.emit(UserListViewState.Success(users))

                } catch (throwable:Throwable) {
                    _userListViewState.emit(UserListViewState.Error(throwable.message ?: ""))
                }
            }

        }
    }


}