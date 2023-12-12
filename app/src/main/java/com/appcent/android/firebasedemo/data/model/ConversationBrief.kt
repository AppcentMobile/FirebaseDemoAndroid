package com.appcent.android.firebasedemo.data.model

data class ConversationBrief(
    val id:String,
    val lastMessage:String,
    val lastMessageTimestamp:Long,
    val userName:String
)