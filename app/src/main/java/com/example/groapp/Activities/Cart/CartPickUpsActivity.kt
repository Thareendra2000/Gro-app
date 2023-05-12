package com.example.groapp.Activities.Cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.Activities.Garden.MyGardensActivity
import com.example.groapp.Activities.HomeActivity
import com.example.groapp.Activities.MyProfileActivity
import com.example.groapp.Adapters.CartPickUpAdapter
import com.example.groapp.Models.OrderModel
import com.example.groapp.R
import com.example.groapp.Services.UserSingleton
import com.google.firebase.database.*

class CartPickUpsActivity : AppCompatActivity() {
    // initialize the UI elements
    private lateinit var pickUpRecycleView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var pickUpList: ArrayList<OrderModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_pick_ups)

        // cart navigation
        var backImg : ImageView = findViewById(R.id.backImg)
        backImg.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        var tvGardens : LinearLayout = findViewById(R.id.tvLeaderboard)
        tvGardens.setOnClickListener{
            val intent = Intent(this, MyGardensActivity::class.java)
            startActivity(intent)
        }
        var tvProfile : LinearLayout = findViewById(R.id.tvProfile)
        tvProfile.setOnClickListener{
            val intent = Intent(this, MyProfileActivity::class.java)
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

        pickUpRecycleView = findViewById(R.id.rvEmp)
        pickUpRecycleView.layoutManager = LinearLayoutManager(this)
        pickUpRecycleView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        pickUpList = arrayListOf<OrderModel>()

        // call the function to get data
        getPickUpData()
    }

    private fun getPickUpData() {

        pickUpRecycleView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val dbRef = FirebaseDatabase.getInstance().getReference("order")
            .orderByChild("userId").equalTo(UserSingleton.uid)

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pickUpList.clear()
                if (snapshot.exists()){
                    for (dataSnap in snapshot.children){
                        val data = dataSnap.getValue(OrderModel::class.java)
                        if (data?.status.toString() == "ACCEPTED") { // Filter by status
                            pickUpList.add(data!!)
                        }
                    }


                    if(pickUpList.size == 0)
                        tvLoadingData.setText("No data to show")
                    val mAdapter = CartPickUpAdapter(pickUpList)
                    pickUpRecycleView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : CartPickUpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                        }
                    })

                    pickUpRecycleView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
                else{
                    tvLoadingData.setText("No data to show")
                    tvLoadingData.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Something went wrong")
            }

        })
    }

}