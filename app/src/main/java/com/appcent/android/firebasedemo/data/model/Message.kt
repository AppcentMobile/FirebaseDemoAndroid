package com.appcent.android.firebasedemo.data.model

data class Message(val timestamp: Long, val text: String, val senderId: String) {
    // Provide a no-argument constructor
    constructor() : this(0, "", "0")
}
