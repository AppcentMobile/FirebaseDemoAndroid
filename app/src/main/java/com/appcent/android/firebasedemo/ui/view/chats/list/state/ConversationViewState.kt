package com.appcent.android.firebasedemo.ui.view.chats.list.state

import com.appcent.android.firebasedemo.data.model.Conversation
import com.appcent.android.firebasedemo.data.model.ConversationBrief

sealed interface ConversationViewState {
    data object Loading : ConversationViewState
    data object Empty : ConversationViewState
    data class Success(val conversations: List<ConversationBrief>) : ConversationViewState
    data class Error(val errorMessage: String) : ConversationViewState
}
