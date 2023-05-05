package com.example.groapp.Models

import java.util.*

enum class OrderStatus {
    PENDING,
    COMPLETED,
    ACCEPTED
}

data class OrderModel(
    val id: String? = null,
    val userId: String? = null,
    val gardenId: String? = null,
    val productId: String? = null,
    val cartId: String? = null,
    val note: String? = null,
    val timestamp: Date,
    val status: OrderStatus? = null
)
