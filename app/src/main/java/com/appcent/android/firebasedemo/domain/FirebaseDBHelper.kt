package com.appcent.android.firebasedemo.domain

import com.appcent.android.firebasedemo.data.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseDBHelper(
    private val databaseReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
)  {


    private val currentUserId: String = firebaseAuth.currentUser?.uid ?: ""


    private fun writeData(path: String, data: Any, completionListener: DatabaseReference.CompletionListener? = null) {
        databaseReference.child(path).setValue(data, completionListener)
    }

    private fun readData(path: String, valueEventListener: ValueEventListener) {
        databaseReference.child(path).addListenerForSingleValueEvent(valueEventListener)
    }

    fun getUserConversations(valueEventListener: ValueEventListener) {
        readData("users/$currentUserId/chats", valueEventListener)
    }

    fun getConversationMessages(conversationId: String, valueEventListener: ValueEventListener) {
        readData("chats/$conversationId/messages", valueEventListener)
    }

    suspend fun getUsersListForMessaging(): List<User> = suspendCoroutine { continuation ->
        readData("users", object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usersList = mutableListOf<User>()

                for (userSnapshot in dataSnapshot.children) {
                    val userId = userSnapshot.key ?: ""
                    val userName = userSnapshot.child("username").getValue(String::class.java) ?: ""

                    // Exclude the current user from the list
                    if (userId != currentUserId) {
                        usersList.add(User(userId, userName))
                    }
                }

                continuation.resume(usersList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                continuation.resumeWithException(databaseError.toException())
            }
        })
    }


    fun addUserToUserNode(userId: String, userData: Map<String, Any>) {
        // Assuming you have a node for each user storing user details
        writeData("users/$userId", userData)
    }

    // Add other database operations as needed

    // Example usage:
    // FirebaseDBHelper.getInstance().getUserConversations(conversationsValueEventListener)
    // FirebaseDBHelper.getInstance().getConversationMessages("chat_id", messagesValueEventListener)
    // FirebaseDBHelper.getInstance().getUsersListForMessaging(usersListValueEventListener)
    // FirebaseDBHelper.getInstance().addUserToUserNode("other_user_id", otherUserData)
}
