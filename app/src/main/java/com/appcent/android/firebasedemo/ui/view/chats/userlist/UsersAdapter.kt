package com.appcent.android.firebasedemo.ui.view.chats.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appcent.android.firebasedemo.data.model.User
import com.appcent.android.firebasedemo.databinding.ItemUserBinding

class UsersAdapter(val onUserSelect: (String) -> Unit) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    private val users: MutableList<User> = mutableListOf()

    fun updateDate(data: List<User>) {
        users.clear()
        users.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.UserViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return UserViewHolder(ItemUserBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onBindViewHolder(holder: UsersAdapter.UserViewHolder, position: Int) {
        holder.bind(user = users[position])
    }


    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var user: User? = null

        init {
            itemView.setOnClickListener {
                user?.userId?.let(onUserSelect)
            }
        }

        fun bind(user: User) {
            this.user = user
            with(binding) {
                textViewUserName.text = "${user.name}(${user.email})"
            }
        }

    }

}