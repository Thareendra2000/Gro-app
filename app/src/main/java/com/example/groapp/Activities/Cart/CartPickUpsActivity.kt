package com.example.groapp.Activities.Cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.Activities.HomeActivity
import com.example.groapp.Adapters.CartPickUpAdapter
import com.example.groapp.Models.OrderModel
import com.example.groapp.R
import com.google.firebase.database.*

class CartPickUpsActivity : AppCompatActivity() {
    private lateinit var pickUpRecycleView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var pickUpList: ArrayList<OrderModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_pick_ups)

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

        pickUpRecycleView = findViewById(R.id.rvEmp)
        pickUpRecycleView.layoutManager = LinearLayoutManager(this)
        pickUpRecycleView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        pickUpList = arrayListOf<OrderModel>()

        getPickUpData()
    }

    private fun getPickUpData() {

        pickUpRecycleView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val dbRef = FirebaseDatabase.getInstance().getReference("order").orderByChild("status").equalTo("ACCEPTED")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pickUpList.clear()
                if (snapshot.exists()){
                    for (dataSnap in snapshot.children){
                        val data = dataSnap.getValue(OrderModel::class.java)
                        pickUpList.add(data!!)
                    }
                    val mAdapter = CartPickUpAdapter(pickUpList)
                    pickUpRecycleView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : CartPickUpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                        }
                    })

                    pickUpRecycleView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}