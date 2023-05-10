package com.example.groapp.Enums

enum class OrderStatus {
    PENDING,
    ACCEPTED,
    COMPLETED,
    REJECTED
}

//Orders -> PENDING
//Once confirmed from orders table
// -> update status to ACCEPTED
//-> update status to REJECTED (if)

//Pickups -> Completed