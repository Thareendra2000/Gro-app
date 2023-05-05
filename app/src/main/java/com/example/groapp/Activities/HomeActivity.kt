package com.example.groapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.groapp.Activities.Cart.CartPendingActivity
import com.example.groapp.Activities.MarketPlace.MarketPlaceActivity
import com.example.groapp.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var tvMarketPlace : LinearLayout = findViewById(R.id.tvMarketPlace)
        tvMarketPlace.setOnClickListener{
            val intent = Intent(this, MarketPlaceActivity::class.java)
            startActivity(intent)
        }
        var tvCart : ImageView = findViewById(R.id.tvCart)
        tvCart.setOnClickListener{
            val intent = Intent(this, CartPendingActivity::class.java)
            startActivity(intent)
        }
    }
}