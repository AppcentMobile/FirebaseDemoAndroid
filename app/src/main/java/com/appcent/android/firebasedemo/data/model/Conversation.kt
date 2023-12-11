package com.appcent.android.firebasedemo.data.model

data class Conversation(val messages: MutableList<Message>, val id: String, val participants: List<String>)
