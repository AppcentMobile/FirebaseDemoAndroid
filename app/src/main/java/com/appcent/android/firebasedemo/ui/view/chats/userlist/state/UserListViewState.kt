package com.appcent.android.firebasedemo.ui.view.chats.userlist.state

import com.appcent.android.firebasedemo.data.model.User

sealed interface UserListViewState {
    data object Loading : UserListViewState
    data object Empty : UserListViewState
    data class Success(val userList: List<User>) : UserListViewState
    data class Error(val errorMessage: String) : UserListViewState
    data class OpenConversation(val conversationId:String) : UserListViewState
}
