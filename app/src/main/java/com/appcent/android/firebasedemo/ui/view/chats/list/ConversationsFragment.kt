package com.appcent.android.firebasedemo.ui.view.chats.list

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.appcent.android.firebasedemo.data.model.ConversationBrief
import com.appcent.android.firebasedemo.databinding.FragmentConversationsBinding
import com.appcent.android.firebasedemo.domain.util.extensions.collectFlow
import com.appcent.android.firebasedemo.ui.base.BaseFragment
import com.appcent.android.firebasedemo.ui.view.chats.list.state.ConversationViewState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConversationsFragment:BaseFragment<FragmentConversationsBinding>() {

    private val viewModel: ConversationsViewModel by viewModels()

    override fun getViewBinding() =
        FragmentConversationsBinding.inflate(layoutInflater)


    override fun delayedInitUi() {
        viewModel.getConversations()
    }

    override fun setObservers() {
        collectFlow(viewModel.viewState) {
            handleViewState(it)
        }
    }

    private fun handleViewState(conversationViewState: ConversationViewState) {
        hideProgress()
        when(conversationViewState) {
            ConversationViewState.Empty -> showEmptyState()
            is ConversationViewState.Error -> showErrorDialog(conversationViewState.errorMessage)
            ConversationViewState.Loading -> showProgress()
            is ConversationViewState.Success -> showConversations(conversationViewState.conversations)
        }
    }

    private fun showConversations(conversations: List<ConversationBrief>) {
        with(binding) {
            emptyState.isVisible = false
            rvChats.isVisible = true
            val adapter = ConversationsAdapter(conversations) {
                nav(ConversationsFragmentDirections.actionConversationsToChat(it))
            }
            rvChats.adapter = adapter
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
            fabNewChat.setOnClickListener {
                nav(ConversationsFragmentDirections.actionConversationsToUserList())
            }
        }
    }

}