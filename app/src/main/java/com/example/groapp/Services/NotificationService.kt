package com.example.groapp.Services

import android.content.ContentValues.TAG
import android.util.Log
import com.example.groapp.Models.NotificationModel
import com.example.groapp.Utils.PseudoCookie
import com.google.firebase.database.*
import java.util.*

class NotificationService {
    private val dbRef = FirebaseDatabase.getInstance().getReference("notifications")
//    private val userId = PseudoCookie.getPseudoCookie().getCookieValue("logged_user_id")
    private val userId = "-NUeURgCQxX2vHkhfi6z"
    fun saveNotifications(title: String, message: String) {
        val id = dbRef.push().key!!
        val timestamp = Date()
        val notification = NotificationModel(id, title, message, timestamp, false, userId)
        dbRef.child(id).setValue(notification)
    }


    /*  how to use this function
        val notificationService = NotificationService()
        notificationService.getAllNotifications { notifications ->
            for (notification in notifications) {
                println("Notification ID: ${notification.id}")
                println("Title: ${notification.title}")
                println("Message: ${notification.message}")
                println("Timestamp: ${notification.timestamp}")
                println("Read: ${notification.isRead}")
                println("User ID: ${notification.userId}")
                println("-------------------------------")
            }
        }
    */
    fun getAllNotifications(callback: (List<NotificationModel>) -> Unit) {
        dbRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notifications = mutableListOf<NotificationModel>()
                for (notificationSnapshot in snapshot.children) {
                    val notification = notificationSnapshot.getValue(NotificationModel::class.java)
                    if (notification != null) {
                        notification.id = notificationSnapshot.key
                        notifications.add(notification)
                    }
                }
                callback(notifications)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("NotificationService", "Database error occurred: ${error.message}")
            }
        })
    }

    fun markNotificationAsRead(notificationId: String) {
        val notificationRef = dbRef.child(notificationId)
        notificationRef.child("isRead").setValue(true)
            .addOnSuccessListener {
                Log.d(TAG, "Notification $notificationId marked as read")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error marking notification $notificationId as read: ${e.message}")
            }
    }


    /*
        get unread notifications
        val notificationService = NotificationService()
        notificationService.getUnreadNotifications { notifications ->
            for (notification in notifications) {
                println("Notification ID: ${notification.id}")
                println("Title: ${notification.title}")
                println("Message: ${notification.message}")
                println("Timestamp: ${notification.timestamp}")
                println("Read: ${notification.isRead}")
                println("User ID: ${notification.userId}")
                println("-------------------------------")
            }
        }
    */
    fun getUnreadNotifications(callback: (List<NotificationModel>) -> Unit) {
        dbRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notifications = mutableListOf<NotificationModel>()
                for (notificationSnapshot in snapshot.children) {
                    val notification = notificationSnapshot.getValue(NotificationModel::class.java)
                    if (notification != null && !notification.isRead!!) {
                        notification.id = notificationSnapshot.key
                        notifications.add(notification)
                    }
                }
                callback(notifications)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("NotificationService", "Database error occurred: ${error.message}")
            }
        })
    }
}
