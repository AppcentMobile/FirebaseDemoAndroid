package com.appcent.android.firebasedemo.ui.view.chats.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appcent.android.firebasedemo.data.model.ConversationBrief
import com.appcent.android.firebasedemo.databinding.ItemConversationBinding

class ConversationsAdapter(
    private val conversations: List<ConversationBrief>,
    private val onSelectConversation: (String) -> Unit
) :
    RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConversationsAdapter.ConversationViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return ConversationViewHolder(ItemConversationBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return conversations.count()
    }

    override fun onBindViewHolder(
        holder: ConversationsAdapter.ConversationViewHolder,
        position: Int
    ) {
        holder.bind(conversation = conversations[position])
    }


    inner class ConversationViewHolder(private val binding: ItemConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(conversation: ConversationBrief) {
            with(binding) {
                itemView.setOnClickListener {
                   onSelectConversation.invoke(conversation.id)
                }
                tvLastMessage.text = conversation.lastMessage
                tvUserName.text = conversation.userName

            }
        }

    }

}