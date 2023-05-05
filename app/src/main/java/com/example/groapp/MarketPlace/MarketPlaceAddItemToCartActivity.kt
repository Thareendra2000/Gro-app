package com.example.groapp.MarketPlace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.groapp.Model.CartModel
import com.example.groapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MarketPlaceAddItemToCartActivity : AppCompatActivity() {
    private lateinit var backImg: ImageView
    private lateinit var prodId: String
    private lateinit var name: String
    private lateinit var quantity: String
    private lateinit var unit: String
    private lateinit var unitPrice: String
    private lateinit var gardenName: String
    private lateinit var description: String
    private lateinit var category: String
    private lateinit var bestBefore: String

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
            quantity = extras.getString("quantity").toString()
            unit = extras.getString("unit").toString()
            unitPrice = extras.getString("unitPrice").toString()
            gardenName = extras.getString("gardenName").toString()
            description = extras.getString("description").toString()
            category = extras.getString("category").toString()
            bestBefore = extras.getString("bestBefore").toString()
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

        btnAddCart.text = "Add $itemCount to Cart, LKR: %.2f".format(unitPrice.toDouble() * itemCount)

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
        val total = unitPrice.toDouble() * amount
        val userId = "1"
        val productId = prodId.toInt() - 1
        val cartId = dbRef.push().key!!
        val cart = CartModel(cartId, userId, productId.toString(), amount.toString(), total.toString(), Date(), "Pending")

        dbRef.child(cartId).setValue(cart)
            .addOnCompleteListener{
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                val newAvailability = (quantity.toInt() - amount).toString()
                val productsRef = FirebaseDatabase.getInstance().getReference("products")
                val productUpdates = mapOf("quantity" to newAvailability)
                productsRef.child(productId.toString()).updateChildren(productUpdates)

            }
            .addOnFailureListener{
                error -> Toast.makeText(this, "Error ${error.message}", Toast.LENGTH_LONG).show()
            }
    }
}
