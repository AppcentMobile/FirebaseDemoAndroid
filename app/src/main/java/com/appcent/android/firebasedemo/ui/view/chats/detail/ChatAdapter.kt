package com.appcent.android.firebasedemo.ui.view.chats.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.appcent.android.firebasedemo.data.model.Message
import com.appcent.android.firebasedemo.databinding.ItemIncomingMessageBinding
import com.appcent.android.firebasedemo.databinding.ItemOutgoingMessageBinding
import timber.log.Timber


class ChatAdapter(private val currentUserId:String) : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    companion object {
        const val VIEW_TYPE_INCOMING = 1
        const val VIEW_TYPE_OUTGOING = 2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_INCOMING -> {
                val binding = ItemIncomingMessageBinding.inflate(inflater, parent, false)
                IncomingMessageViewHolder(binding)
            }

            VIEW_TYPE_OUTGOING -> {
                val binding = ItemOutgoingMessageBinding.inflate(inflater, parent, false)
                OutgoingMessageViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)

        when (holder) {
            is IncomingMessageViewHolder -> holder.bind(message)
            is OutgoingMessageViewHolder -> holder.bind(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)

        return if (message.senderId == currentUserId) {
            VIEW_TYPE_OUTGOING
        } else {
            VIEW_TYPE_INCOMING
        }
    }

    class IncomingMessageViewHolder(private val binding: ItemIncomingMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvMessage.text = message.text
        }
    }

    class OutgoingMessageViewHolder(private val binding: ItemOutgoingMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvMessage.text = message.text
        }
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}