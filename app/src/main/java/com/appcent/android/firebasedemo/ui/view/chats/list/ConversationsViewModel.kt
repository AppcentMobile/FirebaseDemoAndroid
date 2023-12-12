package com.appcent.android.firebasedemo.ui.view.chats.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.appcent.android.firebasedemo.data.model.ConversationBrief
import com.appcent.android.firebasedemo.domain.FirebaseDBHelper
import com.appcent.android.firebasedemo.domain.repository.AuthRepository
import com.appcent.android.firebasedemo.ui.view.chats.detail.state.ChatViewState
import com.appcent.android.firebasedemo.ui.view.chats.list.state.ConversationViewState
import com.mkhakpaki.sinatobechanged.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel  @Inject constructor(
    private val firebaseDBHelper: FirebaseDBHelper
) : BaseViewModel() {

    private val _viewState = MutableSharedFlow<ConversationViewState>()
    val viewState = _viewState.asSharedFlow()
    fun getConversations() {
        viewModelScope.launch {
            _viewState.emit(ConversationViewState.Loading)
            firebaseDBHelper.getUserConversations().collect { conversations ->
                if (conversations != null) {
                    if (conversations.isEmpty()) {
                        _viewState.emit(ConversationViewState.Empty)
                    } else {
                        _viewState.emit(ConversationViewState.Success(conversations))
                    }
                }
            }
        }
    }



}