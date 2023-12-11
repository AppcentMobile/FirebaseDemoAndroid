package com.appcent.android.firebasedemo.data.model

data class Conversation(
    val messages: Map<String, Message>? = null,
    val participants: List<String>
){
    // Provide a no-argument constructor
    constructor() : this(mapOf(), emptyList())
}