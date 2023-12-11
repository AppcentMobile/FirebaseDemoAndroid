package com.appcent.android.firebasedemo.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appcent.android.firebasedemo.data.model.Conversation
import com.appcent.android.firebasedemo.data.model.ConversationBrief
import com.appcent.android.firebasedemo.data.model.Message
import com.appcent.android.firebasedemo.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun getUserConversations(): Flow<List<ConversationBrief>?> {
        val conversationsFlow = MutableStateFlow<List<ConversationBrief>?>(null)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val conversations = mutableListOf<ConversationBrief>()

                // Iterate through user's chats
                snapshot.child("users").child(currentUserId)
                    .child("chats").children.forEach { chatSnapshot ->
                        var userName = ""
                        val chatId = chatSnapshot.value.toString()

                        // Get conversation details
                        val conversationSnapshot = snapshot.child("conversations").child(chatId)
                        val lastMessage = getLastMessage(conversationSnapshot)
                        val participants =
                            conversationSnapshot.child("participants").children.map { it.value.toString() }
                        val otherParticipantId = participants.find { it != currentUserId }
                        otherParticipantId?.let {
                            val user = snapshot.child("users").child(it)
                            userName = "${user.child("name").value}(${user.child("email").value})"
                        }
                        // Create ConversationBrief and add to the list
                        val conversationBrief = ConversationBrief(
                            id = chatId,
                            lastMessage = lastMessage?.text ?: "",
                            lastMessageTimestamp = lastMessage?.timestamp ?: 0,
                            userName = userName
                        )
                        conversations.add(conversationBrief)
                    }

                // Sort conversations by timestamp (assuming ConversationBrief has a timestamp property)
                conversations.sortByDescending { it.lastMessageTimestamp }
                conversationsFlow.value = conversations
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error if needed
            }
        }

        // Attach the ValueEventListener to the database reference
        databaseReference.addValueEventListener(valueEventListener)

        return conversationsFlow.asStateFlow()
    }


    suspend fun getConversation(conversationId: String): Conversation? =
        suspendCoroutine { continuation ->
            readData("conversations/$conversationId", object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val conversation = snapshot.getValue(Conversation::class.java)
                    continuation.resume(conversation)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    continuation.resumeWithException(databaseError.toException())
                }
            })
        }

    fun observeConversationChanges(conversationId: String): LiveData<List<Message>> {
        val liveData = MutableLiveData<List<Message>>()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(Conversation::class.java)?.messages?.values?.toList()
                    ?.let { messageList ->
                        liveData.postValue(messageList.sortedBy { it.timestamp })
                    }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error if needed
            }
        }

        // Attach the ValueEventListener to the database reference
        val conversationRef = databaseReference.child("conversations").child(conversationId)
        conversationRef.addValueEventListener(valueEventListener)

        // Add a cleanup mechanism when the LiveData is cleared
        liveData.observeForever {
            // Remove the ValueEventListener when there are no observers
            if (!liveData.hasObservers()) {
                conversationRef.removeEventListener(valueEventListener)
            }
        }

        return liveData
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

            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {

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

        val conversation = Conversation(null, listOf(user1Id, user2Id))
        writeData(
            "conversations/$conversationId", conversation
        ) { error, _ ->
            error?.let {
                continuation.resumeWithException(it.toException())
            }
        }
        continuation.resume(conversationId)
    }

    fun addMessageToConversation(conversationId: String, message: Message) {
        val messagesRef =
            databaseReference.child("conversations").child(conversationId).child("messages")

        // Generate a unique key for the new message
        val messageId = messagesRef.push().key ?: ""

        // Set the message data under the generated key
        messagesRef.child(messageId).setValue(message)

    }


    private fun getLastMessage(conversationSnapshot: DataSnapshot): Message? {
        val messagesSnapshot = conversationSnapshot.child("messages")
        val latestMessageSnapshot = messagesSnapshot.children.maxByOrNull {
            it.child("timestamp").getValue(Long::class.java) ?: 0
        }

        return latestMessageSnapshot?.getValue(Message::class.java)
    }
}
