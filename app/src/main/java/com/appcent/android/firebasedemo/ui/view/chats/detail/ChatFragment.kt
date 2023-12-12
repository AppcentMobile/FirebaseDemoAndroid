package com.appcent.android.firebasedemo.ui.view.chats.detail

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.appcent.android.firebasedemo.databinding.FragmentChatBinding
import com.appcent.android.firebasedemo.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.navArgs
import com.appcent.android.firebasedemo.data.model.Conversation
import com.appcent.android.firebasedemo.domain.util.extensions.collectFlow
import com.appcent.android.firebasedemo.ui.view.chats.detail.state.ChatViewState
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private val viewModel: ChatViewModel by viewModels()

    private val navArgs: ChatFragmentArgs by navArgs()

    private lateinit var chatAdapter: ChatAdapter
    override fun getViewBinding() = FragmentChatBinding.inflate(layoutInflater)

    override fun getFragmentArguments() {
        viewModel.setConversationId(navArgs.conversationId)
    }
    override fun initUi() {
        chatAdapter = ChatAdapter(viewModel.getCurrentUserId())
        binding.rvChats.adapter = chatAdapter
    }
    override fun delayedInitUi() {
        viewModel.loadConversation()
    }

    override fun setObservers() {
        collectFlow(viewModel.viewState) {
            handleViewState(it)
        }

        viewModel.observeConversation().observe(viewLifecycleOwner) {
            activity?.runOnUiThread {
                chatAdapter.submitList(it)
                if (chatAdapter.itemCount > 0) {
                    binding.rvChats.smoothScrollToPosition(chatAdapter.itemCount)
                }
            }
        }
    }

    private fun handleViewState(chatViewState: ChatViewState) {
        hideProgress()
        when(chatViewState) {
            ChatViewState.Empty -> showEmptyState()
            is ChatViewState.Error -> showErrorDialog(chatViewState.errorMessage)
            ChatViewState.Loading -> showProgress()
            is ChatViewState.Success -> showConversation(chatViewState.conversation)
        }
    }

    private fun showConversation(conversation: Conversation) {
        with(binding) {
            emptyState.isVisible = false
            rvChats.isVisible = true
        }
    }

    private fun showEmptyState() {
        with(binding) {
            emptyState.isVisible = true
            rvChats.isVisible = false
        }
    }

    override fun setClickListeners() {
        with(binding) {
            btnSend.setOnClickListener {
                if (input.text?.toString()?.isBlank() == false) {
                    viewModel.sendMessage(input.text.toString())
                    input.text = null
                }
            }
            input.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (chatAdapter.itemCount > 0) {
                        rvChats.smoothScrollToPosition(chatAdapter.itemCount - 1)
                    }
                }
            }
        }
    }
}