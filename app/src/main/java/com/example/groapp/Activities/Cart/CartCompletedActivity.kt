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
import com.example.groapp.Activities.Order.RateItemActivity
import com.example.groapp.Adapters.CartCompletedAdapter
import com.example.groapp.Models.EmployeeModel
import com.example.groapp.Models.OrderModel
import com.example.groapp.R
import com.google.firebase.database.*

class CartCompletedActivity : AppCompatActivity() {
    private lateinit var completedRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var completedList: ArrayList<OrderModel>
    private lateinit var dbRef: DatabaseReference

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

        completedRecyclerView = findViewById(R.id.rvEmp)
        completedRecyclerView.layoutManager = LinearLayoutManager(this)
        completedRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        completedList = arrayListOf<OrderModel>()

        getCompletedData()
    }

    private fun getCompletedData() {

        completedRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val dbRef = FirebaseDatabase.getInstance().getReference("order").orderByChild("status").equalTo("COMPLETED")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                completedList.clear()
                if (snapshot.exists()){
                    for (dataSnap in snapshot.children){
                        val data = dataSnap.getValue(OrderModel::class.java)
                        completedList.add(data!!)
                    }
                    val mAdapter = CartCompletedAdapter(completedList)
                    completedRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : CartCompletedAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@CartCompletedActivity, RateItemActivity::class.java)
                            intent.putExtra("cartId", completedList[position].id)
                            intent.putExtra("userId", completedList[position].userId)
                            intent.putExtra("gardenId", completedList[position].gardenId)
                            intent.putExtra("productId", completedList[position].productId)
                            startActivity(intent)
                        }
                    })

                    completedRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}