package com.example.groapp.Activities.Order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.Activities.Garden.GardenDetailsOwnerViewActivity
import com.example.groapp.Activities.MyProfileActivity
import com.example.groapp.Adapters.ManageOrdersAdapter
import com.example.groapp.Adapters.ManagePickUpsAdapter
import com.example.groapp.Models.OrderModel
import com.example.groapp.R
import com.example.groapp.Services.UserSingleton
import com.example.groapp.Utils.PseudoCookie
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageOrdersActivity : AppCompatActivity() {
    private lateinit var backBtn : ImageView
    private lateinit var ordersTabBtn : Button
    private lateinit var pickUpsTabTabBtn : Button
    private lateinit var ordersItemsRv : RecyclerView
    private lateinit var tvLoadingData : TextView
    private lateinit var ordersList : ArrayList<OrderModel>
    private lateinit var userID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_orders)
        userID = UserSingleton.uid.toString()
        Log.i("Logged User ID", userID)

        backBtn = findViewById(R.id.backImg)
        backBtn.setOnClickListener{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }

        ordersTabBtn = findViewById(R.id.ordersTabBtn)
        ordersTabBtn.setOnClickListener {
            val intent = Intent(this, ManageOrdersActivity::class.java)
            startActivity(intent)
        }
        pickUpsTabTabBtn = findViewById(R.id.pickupsTabBtn)
        pickUpsTabTabBtn.setOnClickListener {
            val intent = Intent(this, ManagePickUpActivity::class.java)
            startActivity(intent)
        }

        ordersItemsRv = findViewById(R.id.ordersItemsRv)
        ordersItemsRv.layoutManager = LinearLayoutManager(this)
        ordersItemsRv.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        ordersList = arrayListOf<OrderModel>()

        getOrdersData()
    }

    private fun getOrdersData() {
        ordersItemsRv.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val dbRef = FirebaseDatabase.getInstance().getReference("order")
            .orderByChild("status").equalTo("PENDING")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ordersList.clear()
                if (snapshot.exists()){
                    for (dataSnap in snapshot.children){
                        val data = dataSnap.getValue(OrderModel::class.java)
                        if(data!!.userId == userID){
                            ordersList.add(data!!)}
                    }

                    if(!(ordersList.size > 0)){
                        tvLoadingData.setText("No Orders")
                    }
                    val mAdapter = ManageOrdersAdapter(ordersList)
                    ordersItemsRv.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ManageOrdersAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                        }
                    })

                    ordersItemsRv.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
                else{
                    tvLoadingData.setText("No Orders")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error", error.message)
            }

        })
    }

}