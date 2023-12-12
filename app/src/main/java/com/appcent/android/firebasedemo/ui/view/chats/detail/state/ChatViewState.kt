package com.appcent.android.firebasedemo.ui.view.chats.detail.state

import com.appcent.android.firebasedemo.data.model.Conversation

sealed interface ChatViewState {
    data object Loading : ChatViewState
    data object Empty : ChatViewState
    data class Success(val conversation: Conversation) : ChatViewState
    data class Error(val errorMessage: String) : ChatViewState
}
