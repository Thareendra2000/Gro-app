package com.example.groapp.Services

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.example.groapp.Models.ProductModel

class ProductService {
    fun getProductById(productId: String, callback: (ProductModel?, String?) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("products/$productId")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(ProductModel::class.java)
                if (product != null) {
                    callback(product, null)
                } else {
                    callback(null, "Product not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, error.message)
            }
        })
    }
}
