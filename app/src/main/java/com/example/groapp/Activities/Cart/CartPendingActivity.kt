package com.example.groapp.Activities.Cart

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.Activities.Garden.GardenListActivity
import com.example.groapp.Activities.Garden.MyGardensActivity
import com.example.groapp.Activities.HomeActivity
import com.example.groapp.Activities.MarketPlace.MarketPlaceActivity
import com.example.groapp.Activities.MyProfileActivity
import com.example.groapp.Adapters.CartPendingAdapter
import com.example.groapp.Models.CartModel
import com.example.groapp.R
import com.example.groapp.Services.UserSingleton
import com.google.firebase.database.*

class CartPendingActivity : AppCompatActivity() {
    // initialize UI elements
    private lateinit var cartPendingRecycleView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var pendingList: ArrayList<CartModel>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_pending)

        // cart navigation
        var backImg: ImageView = findViewById(R.id.backImg)
        backImg.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        var tvGardens : LinearLayout = findViewById(R.id.tvLeaderboard)
        tvGardens.setOnClickListener{
            val intent = Intent(this, GardenListActivity::class.java)
            startActivity(intent)
        }
        var tvMarketPlace : LinearLayout = findViewById(R.id.tvMarketPlace)
        tvMarketPlace.setOnClickListener{
            val intent = Intent(this, MarketPlaceActivity::class.java)
            startActivity(intent)
        }
        var tvProfile : LinearLayout = findViewById(R.id.tvProfile)
        tvProfile.setOnClickListener{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }
        var tvPending: Button = findViewById(R.id.tvPending)
        tvPending.setOnClickListener {
            val intent = Intent(this, CartPendingActivity::class.java)
            startActivity(intent)
        }
        var tvPickUp: Button = findViewById(R.id.tvPickUp)
        tvPickUp.setOnClickListener {
            val intent = Intent(this, CartPickUpsActivity::class.java)
            startActivity(intent)
        }
        var tvCompleted: Button = findViewById(R.id.tvCompleted)
        tvCompleted.setOnClickListener {
            val intent = Intent(this, CartCompletedActivity::class.java)
            startActivity(intent)
        }

        // set values
        cartPendingRecycleView = findViewById(R.id.rvEmp)
        cartPendingRecycleView.layoutManager = LinearLayoutManager(this)
        cartPendingRecycleView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        pendingList = arrayListOf<CartModel>()

        // call to get pending cart items
        getCartPending()
    }

    private fun getCartPending() {

        cartPendingRecycleView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val userId = UserSingleton.uid.toString()

        val dbRef = FirebaseDatabase.getInstance().getReference("cart").orderByChild("userId").equalTo(userId)

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingList.clear()
                if (snapshot.exists()) {
                    for (cartSnap in snapshot.children) {
                        val data = cartSnap.getValue(CartModel::class.java)
                        if (data?.status.toString() == "PENDING") { // only add data with PENDING status
                            pendingList.add(data!!)
                        }
                    }
                    val mAdapter = CartPendingAdapter(pendingList)
                    cartPendingRecycleView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object :
                        CartPendingAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(
                                this@CartPendingActivity,
                                CartPendingCheckoutActivity::class.java
                            )
                            intent.putExtra("cartId", pendingList[position].id)
                            intent.putExtra("userId", pendingList[position].userId)
                            intent.putExtra("gardenId", pendingList[position].garden_id)
                            intent.putExtra("productId", pendingList[position].productId)
                            intent.putExtra("price", pendingList[position].totalPrice)
                            startActivity(intent)
                        }
                    })

                    cartPendingRecycleView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("error", error.message)
            }
        })
    }
}
