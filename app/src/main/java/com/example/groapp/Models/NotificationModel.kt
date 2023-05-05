package com.example.groapp.Models

import java.util.Date

data class NotificationModel (
    var id: String ?= null,
    val title: String ?= null,
    val message: String ?= null,
    val timestamp: Date ?= null,
    val isRead: Boolean ?= null,
    val userId: String ?= null
)