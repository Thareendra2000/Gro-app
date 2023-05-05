package com.example.groapp.Activities.Cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.groapp.Models.*
import com.example.groapp.R
import com.example.groapp.Services.NotificationService
import com.google.firebase.database.*
import java.util.*

class CartPendingCheckoutActivity : AppCompatActivity() {
    private lateinit var cartId : String
    private lateinit var userId : String
    private lateinit var gardenId : String
    private lateinit var productId : String
    private lateinit var price : String
    private lateinit var note : String
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_pending_checkout)

        // pending checkout navigation
        var btnGoBack: ImageView = findViewById(R.id.btnGoBack)
        btnGoBack.setOnClickListener {
            val intent = Intent(this, CartPendingActivity::class.java)
            startActivity(intent)
        }

        var tvItemName : TextView = findViewById(R.id.tvItemName)
        var tvItemPrice : TextView = findViewById(R.id.tvItemPrice)
        var tvItemNote : EditText = findViewById(R.id.tvItemNote)
        var tvDeleteButton : ImageButton = findViewById(R.id.tvDeleteButton)
        var tvBtnCheckout : Button = findViewById(R.id.tvBtnCheckout)

        val extras = intent.extras
        cartId = extras?.getString("cartId").toString()
        userId = extras?.getString("userId").toString()
        gardenId = extras?.getString("gardenId").toString()
        productId = extras?.getString("productId").toString()
        price = extras?.getString("price").toString()


        productId?.let { FirebaseDatabase.getInstance().getReference("products").child(it) }
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Handle the retrieved garden data here
                    if (dataSnapshot.exists()) {
                        val product = dataSnapshot.getValue(ProductModel::class.java)
                        // Do something with the retrieved garden data
                        val itemName = product?.name
                        tvItemName.text = itemName
                    } else {
                        // Handle the case where the garden data does not exist
                        println("Error")
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error case here
                }
            })

        tvItemPrice.text = "LKR: " + price

        tvDeleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Item")
            builder.setMessage("Are you sure you want to delete this item?")
            builder.setPositiveButton("Yes") { dialog, which ->
                deleteCartItem()
            }
            builder.setNegativeButton("No") { dialog, which ->
                // Do nothing
            }
            builder.show()
        }


        tvBtnCheckout.setOnClickListener {
            note = tvItemNote.text.toString()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm Checkout")
            builder.setMessage("Are you sure you want to proceed with the checkout?")
            builder.setPositiveButton("Yes") { dialog, which ->
                saveOrderData()
            }
            builder.setNegativeButton("No") { dialog, which ->
                // Do nothing
            }
            builder.show()
        }
    }

    private fun deleteCartItem(){
        // reset the quantity in product
        // get the quantity from cart
        val dbRef = FirebaseDatabase.getInstance().getReference("cart").child(cartId)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val cart = dataSnapshot.getValue(CartModel::class.java)
                    val quantity : Int = cart?.quantity?.toInt() ?: 0
                    if (quantity != null) {
                        updateProductQuantity(productId, quantity)
                    }
                } else {
                    // Handle the case where the cart item does not exist
                    println("Error")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error case here
            }
        })

        // delete the cart item
    }

    private fun saveOrderData() {
        dbRef = FirebaseDatabase.getInstance().getReference("order")
        val id = dbRef.push().key!!
        val order = OrderModel(
            id = id,
            userId = userId,
            gardenId = gardenId,
            productId = productId,
            cartId = cartId,
            note = note,
            timestamp = Date(),
            status = OrderStatus.PENDING
        )
        dbRef.child(id).setValue(order).addOnSuccessListener {
            // update the cart status
            val dbRef = FirebaseDatabase.getInstance().getReference("cart").child(cartId)
            dbRef.child("status").setValue("Ordered").addOnSuccessListener {
                val notificationService = NotificationService()
                notificationService.saveNotifications("Item added to the cart", "Item has been added to the cart")
            }.addOnFailureListener { error ->
                println("Error updating cart status: ${error.message}")
            }

            Handler().postDelayed({
                val intent = Intent(this, CartPendingActivity::class.java)
                startActivity(intent)
            }, 1000)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Error ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateProductQuantity(productId: String, newQuantity: Int) {
        val dbRef = FirebaseDatabase.getInstance().getReference("products").child(productId)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val product = dataSnapshot.getValue(ProductModel::class.java)

                    // Get the existing quantity and add the new quantity
                    var existingQuantity = product?.quantity?.toInt()
                    val updatedQuantity = existingQuantity?.plus(newQuantity)

                    // Update the quantity in the database
                    dbRef.child("quantity").setValue(updatedQuantity.toString())
                        .addOnSuccessListener {
                            // Quantity updated successfully
                            deleteTheCartItem();
                            println("Product quantity updated successfully.")
                        }.addOnFailureListener { error ->
                            // Error updating the quantity
                            println("Error updating product quantity: ${error.message}")
                        }
                } else {
                    // Product does not exist
                    println("Product does not exist.")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error case here
                println("Error retrieving product data: ${databaseError.message}")
            }
        })
    }

    fun deleteTheCartItem() {
        val database = FirebaseDatabase.getInstance()
        val cartRef = database.getReference("cart").child(cartId)
        cartRef.removeValue().addOnSuccessListener {
            val notificationService = NotificationService()
            notificationService.saveNotifications("Item deleted from the cart", "Item has been deleted from the cart")

            Handler().postDelayed({
                val intent = Intent(this, CartPendingActivity::class.java)
                startActivity(intent)
            }, 1000)
        }
    }

}