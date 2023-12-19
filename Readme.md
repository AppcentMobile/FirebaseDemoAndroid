# FirebaseChat Project

## Overview

The FirebaseChat project is a chat application that utilizes Firebase Authentication for user management and Firebase Realtime Database for handling messages between users. This project enables users to engage in conversations, send messages, and maintain a chat history in real-time.

## Technologies Used

- Firebase Authentication: Manages user authentication.
- Firebase Realtime Database: Stores and retrieves messages and user data.
- Firebase Crashlytics: For catching any unwanted crashes and issues
- Jetpack Navigation : As our main tool for navigating between screens 
- Hilt: For dependency Injection


## Usage
Create your firebase project and add google-services.json to this project 
then add database rules to your firebase realtime database rules

## Firebase Realtime Database Rules

```json
{
  "rules": {
    ".read": "auth != null",
    ".write": "auth != null",

    "conversations": {
      "$conversationId": {
        ".read": "auth != null && (
          data.child('participants').child(auth.uid).exists() ||
          root.child('users').child(auth.uid).child('chats').child($conversationId).exists()
        )",
        ".write": "auth != null && (
          data.child('participants').child(auth.uid).exists() ||
          root.child('users').child(auth.uid).child('chats').child($conversationId).exists()
        )",
        "messages": {
          ".read": "auth != null && (
            root.child('conversations').child($conversationId).child('participants').child(auth.uid).exists() ||
            root.child('users').child(auth.uid).child('chats').child($conversationId).exists()
          )",
          ".write": "auth != null && (
            root.child('conversations').child($conversationId).child('participants').child(auth.uid).exists() ||
            root.child('users').child(auth.uid).child('chats').child($conversationId).exists()
          )"
        }
      }
    },

    "users": {
      "$userId": {
        ".read": "auth != null && auth.uid === $userId",
        ".write": "auth != null && auth.uid === $userId"
      }
    }
  }
}


