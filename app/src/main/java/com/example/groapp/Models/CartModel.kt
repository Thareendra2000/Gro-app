package com.example.groapp.Models

import com.example.groapp.Enums.CartStatus
import java.util.Date

data class CartModel (
    var id: String ?= null,
    var userId: String ?= null,
    var productId: String ?= null,
    var quantity: String ?= null,
    var totalPrice: String ?= null,
    var date: Date ?= null,
    var status: CartStatus ?= null,
    var image_url : String ?= null,
    var garden_id : String ?= null
)