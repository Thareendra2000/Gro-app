package com.example.groapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.example.groapp.Activities.MarketPlace.MarketPlaceActivity
import com.example.groapp.R

class HomeActivity : AppCompatActivity() {
    private lateinit var tvMarketPlace: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tvMarketPlace = findViewById(R.id.tvMarketPlace)
        tvMarketPlace.setOnClickListener{
            val intent = Intent(this, MarketPlaceActivity::class.java)
            startActivity(intent)
        }
    }
}