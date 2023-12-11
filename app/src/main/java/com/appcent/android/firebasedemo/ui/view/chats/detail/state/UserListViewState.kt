package com.appcent.android.firebasedemo.ui.view.chats.detail.state

import com.appcent.android.firebasedemo.data.model.Conversation
import com.appcent.android.firebasedemo.data.model.User
import com.appcent.android.firebasedemo.ui.view.chats.userlist.state.UserListViewState

sealed interface ChatViewState {
    data object Loading : ChatViewState
    data object Empty : ChatViewState
    data class Success(val conversation: Conversation) : ChatViewState
    data class Error(val errorMessage: String) : ChatViewState
}
