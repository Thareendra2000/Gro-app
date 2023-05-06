package com.example.groapp.Models

import com.example.groapp.Enums.OrderStatus
import java.util.*

data class OrderModel(
    val id: String? = null,
    val userId: String? = null,
    val gardenId: String? = null,
    val productId: String? = null,
    val cartId: String? = null,
    val note: String? = null,
    val timestamp: Date = Date(),
    val status: OrderStatus? = null
)

