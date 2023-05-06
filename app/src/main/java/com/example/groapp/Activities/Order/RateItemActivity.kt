package com.example.groapp.Activities.Order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.groapp.R

class RateItemActivity : AppCompatActivity() {
    private lateinit var reviewImage : ImageView
    private lateinit var reviewGardenName : TextView
    private lateinit var reviewItemName : TextView
    private lateinit var reviewItemDescription : TextView
    private lateinit var reviewOrderQuantity : TextView
    private lateinit var reviewItemUnitPrice : TextView
    private lateinit var reviewItemUnit : TextView
    private lateinit var reviewItemBestBefore : TextView

    private lateinit var start1 : ImageView
    private lateinit var start2 : ImageView
    private lateinit var start3 : ImageView
    private lateinit var start4 : ImageView
    private lateinit var start5 : ImageView

    private lateinit var rateBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_item)

        val productId = intent.getStringExtra("productId")
        val cartId = intent.getStringExtra("cartId")
        val userId = intent.getStringExtra("userId")
        val gardenId = intent.getStringExtra("gardenId")


        reviewImage = findViewById(R.id.reviewImage)
        reviewGardenName = findViewById(R.id.reviewGardenName)
        reviewItemName = findViewById(R.id.reviewItemName)
        reviewItemDescription = findViewById(R.id.reviewItemDescription)
        reviewOrderQuantity = findViewById(R.id.reviewOrderQuantity)
        reviewItemUnitPrice = findViewById(R.id.reviewItemUnitPrice)
        reviewItemUnit = findViewById(R.id.reviewItemUnit)
        reviewItemBestBefore = findViewById(R.id.reviewItemBestBefore)

        start1 = findViewById(R.id.star1)
        start2 = findViewById(R.id.star2)
        start3 = findViewById(R.id.star3)
        start4 = findViewById(R.id.star4)
        start5 = findViewById(R.id.star5)

        rateBtn = findViewById(R.id.reviewImage)



    }
}