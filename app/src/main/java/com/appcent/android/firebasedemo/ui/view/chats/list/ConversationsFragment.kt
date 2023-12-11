package com.appcent.android.firebasedemo.ui.view.chats.list

import com.appcent.android.firebasedemo.databinding.FragmentConversationsBinding
import com.appcent.android.firebasedemo.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConversationsFragment:BaseFragment<FragmentConversationsBinding>() {
    override fun getViewBinding() =
        FragmentConversationsBinding.inflate(layoutInflater)


    override fun setClickListeners() {
        with(binding) {
            fabNewChat.setOnClickListener {
                nav(ConversationsFragmentDirections.actionConversationsToUserList())
            }
        }
    }

}