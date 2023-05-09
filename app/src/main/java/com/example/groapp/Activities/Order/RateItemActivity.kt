package com.example.groapp.Activities.Order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.groapp.Activities.Cart.CartCompletedActivity
import com.example.groapp.Models.GardenModel
import com.example.groapp.Models.ProductModel
import com.example.groapp.Models.RatingModel
import com.example.groapp.R
import com.example.groapp.Utils.DateUtil
import com.example.groapp.Utils.PseudoCookie
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RateItemActivity : AppCompatActivity() {
    private lateinit var reviewImage: ImageView
    private lateinit var reviewGardenName: TextView
    private lateinit var reviewItemName: TextView
    private lateinit var reviewItemDescription: TextView
    private lateinit var reviewOrderQuantity: TextView
    private lateinit var reviewItemUnitPrice: TextView
    private lateinit var reviewItemUnit: TextView
    private lateinit var reviewItemBestBefore: TextView

    private lateinit var btnIncrease: Button
    private lateinit var btnDecrease: Button
    private lateinit var tvRateCount: TextView

    private lateinit var rateBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_item)

        var ratingCount = 1

        val productId = intent.getStringExtra("productId")
        val cartId = intent.getStringExtra("cartId")
        val userId = intent.getStringExtra("userId")
        val gardenId = intent.getStringExtra("gardenId")


        reviewImage = findViewById(R.id.reviewImage) // done
        reviewGardenName = findViewById(R.id.reviewGardenName) // done
        reviewItemName = findViewById(R.id.reviewItemName) // done
        reviewItemDescription = findViewById(R.id.reviewItemDescription) // done
        reviewOrderQuantity = findViewById(R.id.reviewOrderQuantity)
        reviewItemUnitPrice = findViewById(R.id.reviewItemUnitPrice) // done
        reviewItemUnit = findViewById(R.id.reviewItemUnit) // done
        reviewItemBestBefore = findViewById(R.id.reviewItemBestBefore) // done

        btnIncrease = findViewById(R.id.btnIncrease)
        btnDecrease = findViewById(R.id.btnDecrease)
        tvRateCount = findViewById(R.id.tvRateCount)

        rateBtn = findViewById(R.id.rateBtn)

        // Set listener for increase button
        btnIncrease.setOnClickListener {
            if (ratingCount < 5) {
                ratingCount++
                tvRateCount.text = ratingCount.toString()
            }
        }

        // Set listener for decrease button
        btnDecrease.setOnClickListener {
            if (ratingCount > 1) {
                ratingCount--
                tvRateCount.text = ratingCount.toString()
            }
        }

        rateBtn.setOnClickListener{
            println(ratingCount)
            val ratingModel = RatingModel(
                userId = PseudoCookie.getPseudoCookie().getCookieValue("logged_user_id"),
                productId = productId,
                rating = ratingCount.toDouble()
            )
            addReviewToFirebase(ratingModel)
            if (productId != null) {
                getReviewsForProduct(productId)
            }

            Handler().postDelayed({
                val intent = Intent(this, CartCompletedActivity::class.java)
                startActivity(intent)
                finish()
            }, 1000)
        }

        productId?.let { FirebaseDatabase.getInstance().getReference("products").child(it) }
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Handle the retrieved garden data here
                    if (dataSnapshot.exists()) {
                        val product = dataSnapshot.getValue(ProductModel::class.java)
                        // Do something with the retrieved garden data
                        reviewItemName.text = product?.name
                        reviewItemDescription.text = product?.description
                        reviewItemUnitPrice.text = product?.unit_price
                        reviewItemUnit.text = product?.unit
                        reviewItemBestBefore.text = product?.best_before?.let {
                            DateUtil.formatDate(
                                it
                            )
                        }
                        product?.img_url?.let {
                            Glide.with(this@RateItemActivity)
                                .load(it)
                                .into(reviewImage)
                        }

                    } else {
                        // Handle the case where the garden data does not exist
                        println("Error")
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error case here
                }
            })

        gardenId?.let { FirebaseDatabase.getInstance().getReference("Garden").child(it) }
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Handle the retrieved garden data here
                    if (dataSnapshot.exists()) {
                        val garden = dataSnapshot.getValue(GardenModel::class.java)
                        // Do something with the retrieved garden data
                        reviewGardenName.text = garden?.name
                    } else {
                        // Handle the case where the garden data does not exist
                        println("Error")
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error case here
                }
            })
    }

    fun addReviewToFirebase(ratingModel: RatingModel) {
        val database = FirebaseDatabase.getInstance()
        val reviewsRef = database.getReference("ratings")

        val reviewId = reviewsRef.push().key
        ratingModel.id = reviewId

        reviewsRef.child(reviewId!!).setValue(ratingModel)
            .addOnSuccessListener {
                Toast.makeText(this, "Review added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                println("Failed to add review: ${e.message}")
            }
    }

    fun getReviewsForProduct(productId: String) {
        val database = FirebaseDatabase.getInstance()
        val reviewsRef = database.getReference("ratings")

        val query = reviewsRef.orderByChild("productId").equalTo(productId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var totalRating = 0.0
                var count = 0

                for (snapshot in dataSnapshot.children) {
                    val ratingModel = snapshot.getValue(RatingModel::class.java)
                    ratingModel?.rating?.let {
                        totalRating += it
                        count++
                    }
                }

                val averageRating = if (count > 0) totalRating / count else 0

                // save the average rating to the product schema as "ratings"
                val productRef = database.getReference("products").child(productId)
                productRef.child("rating").setValue(averageRating)
                    .addOnSuccessListener {
                        println("Average rating saved successfully")
                    }
                    .addOnFailureListener { e ->
                        println("Failed to save average rating: ${e.message}")
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to get reviews: ${error.message}")
            }
        })
    }

}