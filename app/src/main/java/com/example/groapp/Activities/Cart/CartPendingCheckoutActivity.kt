package com.example.groapp.Activities.Cart

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.groapp.Activities.NotificationActivity
import com.example.groapp.Enums.CartStatus
import com.example.groapp.Enums.OrderStatus
import com.example.groapp.Models.*
import com.example.groapp.R
import com.example.groapp.Services.NotificationService
import com.example.groapp.Utils.PseudoCookie
import com.google.firebase.database.*
import java.util.*
import kotlin.random.Random

class CartPendingCheckoutActivity : AppCompatActivity() {
    private lateinit var cartId : String
    private lateinit var userId : String
    private lateinit var gardenId : String
    private lateinit var productId : String
    private lateinit var price : String
    private lateinit var note : String
    private lateinit var dbRef : DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
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
        userId = PseudoCookie.getPseudoCookie().getCookieValue("logged_user_id")
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(title: String, message: String) {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("test_channel_id", name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(this, NotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, "test_channel_id")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission( this@CartPendingCheckoutActivity, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED ) {
                return
            }
            notify(Random.nextInt(1200), builder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveOrderData() {
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
            val dbRef = FirebaseDatabase.getInstance().getReference("cart").child(cartId)
            dbRef.child("status").setValue(CartStatus.ORDERED).addOnSuccessListener {
                val notificationService = NotificationService()
                notificationService.saveNotifications("Item added to the cart", "Item has been added to the cart")

//                sendNotification("Added to cart" , "Item has been added to the cart")

            }.addOnFailureListener { error ->
                println("Error updating cart status: ${error.message}")
            }

            Handler().postDelayed({
                val intent = Intent(this, CartPendingCheckoutSuccessActivity::class.java)
                startActivity(intent)
            }, 1000)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Error ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun updateProductQuantity(productId: String, newQuantity: Int) {
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
            notificationService.saveNotifications(
                "Item deleted from the cart",
                "Item has been deleted from the cart"
            )

            Handler().postDelayed({
                val intent = Intent(this, CartPendingActivity::class.java)
                startActivity(intent)
            }, 1000)
        }
    }
}