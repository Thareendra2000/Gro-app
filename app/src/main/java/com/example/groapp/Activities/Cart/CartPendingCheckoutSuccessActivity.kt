package com.example.groapp.Activities.Cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.groapp.Activities.MarketPlace.MarketPlaceActivity
import com.example.groapp.R

class CartPendingCheckoutSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_pending_checkout_success)

        var exploreMoreButton : Button = findViewById(R.id.exploreMoreButton)
        exploreMoreButton.setOnClickListener{
            val intent = Intent(this, MarketPlaceActivity::class.java)
            startActivity(intent)
        }
    }
}