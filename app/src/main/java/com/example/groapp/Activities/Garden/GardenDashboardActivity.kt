package com.example.groapp.Activities.Garden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.example.groapp.Activities.HomeActivity
import com.example.groapp.Activities.MarketPlace.MarketPlaceActivity
import com.example.groapp.Activities.MyProfileActivity
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


        //nav bar intents
        var accountBtn : LinearLayout = findViewById(R.id.tvProfile)
        accountBtn.setOnClickListener{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }
        var tvMarketPlace : LinearLayout = findViewById(R.id.tvMarketPlace)
        tvMarketPlace.setOnClickListener{
            val intent = Intent(this, MarketPlaceActivity::class.java)
            startActivity(intent)
        }
        var gardens : LinearLayout = findViewById(R.id.tvLeaderboard)
        gardens.setOnClickListener {
            val intent = Intent(this, GardenListActivity::class.java)
            startActivity(intent)
        }
        var home : LinearLayout = findViewById(R.id.tvHome)
        home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

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