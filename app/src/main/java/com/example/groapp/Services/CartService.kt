package com.example.groapp.Services

import com.example.groapp.Models.CartModel
import com.google.firebase.database.*

class CartService {

    fun getCartDetails(cartId: String, callback: (CartModel?, String?) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("cart/$cartId")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cart = snapshot.getValue(CartModel::class.java)
                if (cart != null) {
                    callback(cart, null)
                } else {
                    callback(null, "Cart not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, error.message)
            }
        })
    }
}
