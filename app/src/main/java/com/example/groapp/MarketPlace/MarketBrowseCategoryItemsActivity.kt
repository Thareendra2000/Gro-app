package com.example.groapp.MarketPlace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.groapp.R

class MarketBrowseCategoryItemsActivity : AppCompatActivity() {
    private lateinit var backImg: ImageView
    private lateinit var activityIntent: Intent
    private lateinit var catNameTextView: TextView
    private lateinit var catName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_browse_category_items)

        backImg = findViewById(R.id.backImg)
        backImg.setOnClickListener {
            val intent = Intent(this, MarketPlaceActivity::class.java)
            startActivity(intent)
        }

        activityIntent = intent
        catName = activityIntent.getStringExtra("catName").toString()
        catNameTextView = findViewById(R.id.catNameTitle)
        catNameTextView.text = catName
    }
}
