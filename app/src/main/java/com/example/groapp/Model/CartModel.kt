package com.example.groapp.Model

import java.util.Date

data class CartModel (
    var id: String ?= null,
    var userId: String ?= null,
    var productId: String ?= null,
    var quantity: String ?= null,
    var totalPrice: String ?= null,
    var date: Date ?= null,
    var status: String ?= null
)