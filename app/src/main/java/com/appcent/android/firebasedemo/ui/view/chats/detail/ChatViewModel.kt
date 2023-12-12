package com.appcent.android.firebasedemo.ui.view.chats.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.appcent.android.firebasedemo.data.model.Message
import com.appcent.android.firebasedemo.domain.FirebaseDBHelper
import com.appcent.android.firebasedemo.domain.repository.AuthRepository
import com.appcent.android.firebasedemo.ui.view.chats.detail.state.ChatViewState
import com.mkhakpaki.sinatobechanged.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firebaseDBHelper: FirebaseDBHelper,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _viewState = MutableSharedFlow<ChatViewState>()
    val viewState = _viewState.asSharedFlow()


    private lateinit var conversationId: String
    fun sendMessage(messageContent: String) {
        val message = Message(
            System.currentTimeMillis(),
            messageContent,
            senderId = getCurrentUserId()
        )
        firebaseDBHelper.addMessageToConversation(conversationId, message)
    }

    fun observeConversation(): LiveData<List<Message>> {
        return firebaseDBHelper.observeConversationChanges(conversationId)
    }

    fun setConversationId(conversationId: String) {
        this.conversationId = conversationId
    }

    fun getCurrentUserId():String {
        return authRepository.currentUser?.uid.orEmpty()
    }

    fun loadConversation() {
        viewModelScope.launch {
            _viewState.emit(ChatViewState.Loading)
            firebaseDBHelper.getConversation(conversationId)?.let {
                _viewState.emit(ChatViewState.Success(it))
            } ?: kotlin.run {
                _viewState.emit(ChatViewState.Empty)
            }
        }
    }

}