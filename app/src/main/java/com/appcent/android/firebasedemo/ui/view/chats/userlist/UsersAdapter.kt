package com.appcent.android.firebasedemo.ui.view.chats.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appcent.android.firebasedemo.data.model.User
import com.appcent.android.firebasedemo.databinding.ItemUserBinding

class UsersAdapter(private val users: List<User>) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
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

            fun bind(user:User) {
                with(binding) {
                    textViewUserName.text = user.userName
                }
            }

    }

}