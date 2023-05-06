package com.example.groapp.Activities.MarketPlace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.groapp.Enums.CartStatus
import com.example.groapp.Models.CartModel
import com.example.groapp.R
import com.example.groapp.Services.NotificationService
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MarketPlaceAddItemToCartActivity : AppCompatActivity() {
    private lateinit var backImg: ImageView

    private lateinit var prodId: String
    private lateinit var name: String
    private lateinit var unit : String
    private var quantity: Double = 0.0
    private var unitPrice: Double = 0.0
    private lateinit var image_url : String
    private lateinit var garden_id : String

    private lateinit var tvItemName : TextView
    private lateinit var tvItemPrice : TextView
    private lateinit var btnIncrease : Button
    private lateinit var tvItemCount : TextView
    private lateinit var btnDecrease : Button
    private lateinit var btnAddCart : Button
    private lateinit var tvAvailability : TextView

    private lateinit var dbRef : DatabaseReference

    private var itemCount : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_place_add_garden_items)

        dbRef = FirebaseDatabase.getInstance().getReference("cart")

        backImg = findViewById(R.id.backImg)
        backImg.setOnClickListener {
            val intent = Intent(this, MarketPlaceActivity::class.java)
            startActivity(intent)
        }

        val extras = intent.extras
        if (extras != null) {
            prodId = extras.getString("prodId").toString()
            name = extras.getString("name").toString()
            quantity = extras.getString("quantity")?.toDouble() ?: 0.0
            unit = extras.getString("unit").toString()
            unitPrice = extras.getString("unitPrice")?.toDouble() ?: 0.0
            image_url = extras.getString("image_url").toString()
            garden_id = extras.getString("gardenId").toString()
        }

        tvItemName = findViewById(R.id.tvItemName)
        tvItemPrice = findViewById(R.id.tvItemPrice)
        btnIncrease = findViewById(R.id.btnIncrease)
        tvItemCount = findViewById(R.id.tvItemCount)
        btnDecrease = findViewById(R.id.btnDecrease)
        btnAddCart = findViewById(R.id.btnAddCart)
        tvAvailability = findViewById(R.id.tvAvailability)

        tvItemName.text = name
        tvItemPrice.text = "Price: LKR $unitPrice"
        tvAvailability.text = "Availability: $quantity $unit"

        if (quantity.toInt() == 0) {
            btnIncrease.isEnabled = false
            btnDecrease.isEnabled = false
            btnAddCart.isEnabled = false
            btnAddCart.text = "Out of Stock"
        }else{
            btnAddCart.text = "Add $itemCount to Cart, LKR: %.2f".format(unitPrice.toDouble() * itemCount)
        }

        btnIncrease.setOnClickListener{
            if (itemCount != quantity.toInt()){
                itemCount++
                tvItemCount.text = itemCount.toString()
                btnAddCart.text = "Add $itemCount to Cart, LKR: %.2f".format(unitPrice.toDouble() * itemCount)
            }
        }
        btnDecrease.setOnClickListener{
            if(itemCount > 1){
                itemCount--
                tvItemCount.text = itemCount.toString()
                btnAddCart.text = "Add $itemCount to Cart, LKR: %.2f".format(unitPrice.toDouble() * itemCount)
            }
        }

        btnAddCart.setOnClickListener {
            saveCartData()
        }
    }

    private fun saveCartData(){
        val amount = itemCount
        val total = unitPrice * amount
        val userId = "-NUeURgCQxX2vHkhfi6z"
        val productId = prodId
        val cartId = dbRef.push().key!!
        val cart = CartModel(cartId, userId, productId, amount.toString(), total.toString(), Date(), CartStatus.PENDING, image_url, garden_id)

        dbRef.child(cartId).setValue(cart)
            .addOnCompleteListener{
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                val newAvailability = (quantity.toInt() - amount).toString()
                val productsRef = FirebaseDatabase.getInstance().getReference("products")
                val productUpdates = mapOf("quantity" to newAvailability)
                productsRef.child(productId).updateChildren(productUpdates)

                val notificationService = NotificationService()
                notificationService.saveNotifications("Item added to the cart", "test item has been added to the cart")

                Handler().postDelayed({
                    val intent = Intent(this, MarketPlaceActivity::class.java)
                    startActivity(intent)
                }, 1000)
            }
            .addOnFailureListener{
                error -> Toast.makeText(this, "Error ${error.message}", Toast.LENGTH_LONG).show()
            }
    }
}
