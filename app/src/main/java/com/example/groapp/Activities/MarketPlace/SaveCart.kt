package com.example.groapp.Activities.MarketPlace

import android.content.Intent
import android.os.Handler
import android.widget.Toast
import com.example.groapp.Enums.CartStatus
import com.example.groapp.Models.CartModel
import com.example.groapp.Services.NotificationService
import com.example.groapp.Utils.PseudoCookie
import com.google.firebase.database.FirebaseDatabase
import java.util.*

data class SaveCart (
    var amount : Int,
    var unitPrice : Double = 0.0,
    var quantity : Double = 0.0,
    var userId :  String = "",
    var productId : String = "",
    var cartId : String = "",
    var image_url : String = "",
    var garden_id : String = "")
{
    public fun saveCartData(): Boolean{
        var total : Double = unitPrice * amount
        var cart = CartModel(
            cartId,
            userId,
            productId,
            amount.toString(),
            total.toString(),
            Date(),
            CartStatus.PENDING,
            image_url,
            garden_id)

        var result = false

        if(quantity.toInt() - amount > 0){

        }
        FirebaseDatabase.getInstance().getReference("cart").child(cartId).setValue(cart)
            .addOnCompleteListener{
                //Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                val newAvailability = (quantity.toInt() - amount).toString()
                val productsRef = FirebaseDatabase.getInstance().getReference("products")
                val productUpdates = mapOf("quantity" to newAvailability)
                productsRef.child(productId).updateChildren(productUpdates)

                val notificationService = NotificationService()
                notificationService.saveNotifications("Item added to the cart", "test item has been added to the cart")

                result = true
//                Handler().postDelayed({
//                    val intent = Intent(this, MarketPlaceActivity::class.java)
//                    startActivity(intent)
//                }, 1000)
            }
            .addOnFailureListener{
//                    error -> Toast.makeText(this, "Error ${error.message}", Toast.LENGTH_LONG).show()
            }

        return result
    }
}