package com.appcent.android.firebasedemo.ui.view.chats.detail

import com.appcent.android.firebasedemo.databinding.FragmentChatBinding
import com.appcent.android.firebasedemo.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment:BaseFragment<FragmentChatBinding>() {
    override fun getViewBinding() = FragmentChatBinding.inflate(layoutInflater)
}