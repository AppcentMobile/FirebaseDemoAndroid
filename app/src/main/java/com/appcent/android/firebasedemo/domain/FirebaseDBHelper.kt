package com.appcent.android.firebasedemo.domain

import com.appcent.android.firebasedemo.data.model.Conversation
import com.appcent.android.firebasedemo.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import timber.log.Timber
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseDBHelper(
    private val databaseReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) {


    private val currentUserId: String = firebaseAuth.currentUser?.uid ?: ""


    private fun writeData(
        path: String,
        data: Any,
        completionListener: DatabaseReference.CompletionListener? = null
    ) {
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

    suspend fun getUsersListForMessaging(query: String? = null): List<User> =
        suspendCoroutine { continuation ->
            readData("users", object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val usersList = mutableListOf<User>()

                    for (userSnapshot in dataSnapshot.children) {
                        val userId = userSnapshot.key ?: ""
                        val email =
                            userSnapshot.child("email").getValue(String::class.java) ?: ""

                        val name =
                            userSnapshot.child("name").getValue(String::class.java) ?: ""

                        // Exclude the current user from the list
                        if (userId != currentUserId) {
                            query?.let {
                                if (name.contains(it) || email.contains(it)) {
                                    usersList.add(User(userId, name, email))
                                }
                            } ?: kotlin.run {
                                usersList.add(User(userId, name, email))
                            }

                        }
                    }

                    continuation.resume(usersList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    continuation.resumeWithException(databaseError.toException())
                }
            })
        }


    fun addUserToUserNode(userId: String, userData: User) {

        writeData("users/$userId", userData, completionListener = object : ValueEventListener,
            DatabaseReference.CompletionListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Timber.i("data changed ${snapshot.children}")
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.i("onCancelled ${error.message}")
            }

            override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                Timber.i("onComplete ${error?.message}")
            }

        })
    }

    suspend fun getConversationId(userId: String): String =
        suspendCoroutine { continuation ->
            readData("users/$currentUserId/chats/$userId", object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value == null) {
                        createConversation(currentUserId, userId, continuation)
                    } else {
                        continuation.resume(dataSnapshot.value.toString())
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    continuation.resumeWithException(databaseError.toException())
                }
            })
        }


    fun createConversation(
        user1Id: String,
        user2Id: String,
        continuation: Continuation<String>
    ) {
        val conversationId = databaseReference.push().key.toString()
        writeData(
            "users/$user1Id/chats/$user2Id", conversationId
        ) { error, _ ->
            error?.let {
                continuation.resumeWithException(it.toException())
            }
        }
        writeData(
            "users/$user2Id/chats/$user1Id", conversationId
        ) { error, _ ->
            error?.let {
                continuation.resumeWithException(it.toException())
            }
        }

        val conversation = Conversation(mutableListOf(), conversationId, listOf())
        writeData(
            "conversations", conversation
        ) { error, _ ->
            error?.let {
                continuation.resumeWithException(it.toException())
            }
        }
        continuation.resume(conversationId)
    }


}
