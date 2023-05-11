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
import com.example.groapp.Activities.MyProfileActivity
import com.example.groapp.Adapters.CartPickUpAdapter
import com.example.groapp.Adapters.ManagePickUpsAdapter
import com.example.groapp.Models.OrderModel
import com.example.groapp.R
import com.example.groapp.Utils.PseudoCookie
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManagePickUpActivity : AppCompatActivity() {
    private lateinit var backBtn : ImageView
    private lateinit var ordersTabBtn : Button
    private lateinit var pickUpsTabTabBtn : Button
    private lateinit var pickupsItemsRV : RecyclerView
    private lateinit var tvLoadingData : TextView
    private lateinit var pickUpList : ArrayList<OrderModel>
    private lateinit var userID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_pick_up)
        userID = PseudoCookie.getPseudoCookie().getCookieValue("logged_user_id")
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

        pickupsItemsRV = findViewById(R.id.pickupsItemsRV)
        pickupsItemsRV.layoutManager = LinearLayoutManager(this)
        pickupsItemsRV.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        pickUpList = arrayListOf<OrderModel>()

        getPickUpData()
    }

    private fun getPickUpData() {
        pickupsItemsRV.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val dbRef = FirebaseDatabase.getInstance().getReference("order")
            .orderByChild("status").equalTo("ACCEPTED")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pickUpList.clear()
                if (snapshot.exists()){
                    for (dataSnap in snapshot.children){
                        val data = dataSnap.getValue(OrderModel::class.java)

                        if(data!!.userId == userID)
                            pickUpList.add(data!!)
                    }

                    if(!(pickUpList.size > 0)){
                        tvLoadingData.setText("No Orders")
                    }
                    val mAdapter = ManagePickUpsAdapter(pickUpList)
                    pickupsItemsRV.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ManagePickUpsAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                        }
                    })

                    pickupsItemsRV.visibility = View.VISIBLE
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