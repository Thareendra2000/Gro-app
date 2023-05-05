package com.example.groapp.Activities.Cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.groapp.Activities.HomeActivity
import com.example.groapp.Activities.MainActivity
import com.example.groapp.R

class CartCompletedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_completed)

        // cart navigation
        var backImg : ImageView = findViewById(R.id.backImg)
        backImg.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        var tvPending : Button = findViewById(R.id.tvPending)
        tvPending.setOnClickListener{
            val intent = Intent(this, CartPendingActivity::class.java)
            startActivity(intent)
        }
        var tvPickUp : Button = findViewById(R.id.tvPickUp)
        tvPickUp.setOnClickListener{
            val intent = Intent(this, CartPickUpsActivity::class.java)
            startActivity(intent)
        }
        var tvCompleted : Button = findViewById(R.id.tvCompleted)
        tvCompleted.setOnClickListener{
            val intent = Intent(this, CartCompletedActivity::class.java)
            startActivity(intent)
        }
    }
}