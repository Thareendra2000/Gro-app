package com.example.groapp.Activities.Garden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.groapp.R
import com.google.firebase.database.*

class GardenDashboardActivity : AppCompatActivity() {

    companion object {
        lateinit var database: DatabaseReference // static property to hold the database reference
    }
    private lateinit var gardensCountTextView: TextView
    private lateinit var productsCountTextView: TextView
    private lateinit var mostProfitableProduct: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garden_dashboard)

    database = FirebaseDatabase.getInstance().reference
    gardensCountTextView = findViewById(R.id.Amount)
    productsCountTextView = findViewById(R.id.pTotal)
    mostProfitableProduct = findViewById(R.id.pName)

    database.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val gardensCount = dataSnapshot.child("Garden").childrenCount
            val productsCount = dataSnapshot.child("products").childrenCount

            gardensCountTextView.text = gardensCount.toString()
            productsCountTextView.text = productsCount.toString()

            var maxProfit = 0.0
            var mostProfitableProductName = ""

            for (productSnapshot in dataSnapshot.child("products").children) {
                val productName = productSnapshot.child("name").value.toString()
                val quantity = productSnapshot.child("quantity").value.toString().toDouble()
                val unitPrice = productSnapshot.child("unit_price").value.toString().toDouble()

                val profit = quantity * unitPrice
                if (profit > maxProfit) {
                    maxProfit = profit
                    mostProfitableProductName = productName
                }
            }

            mostProfitableProduct.text = "$mostProfitableProductName"
        }



        override fun onCancelled(databaseError: DatabaseError) {
            // Handle errors
        }
    })

    }




}