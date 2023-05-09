package com.example.groapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.Adapters.NotificationAdapter
import com.example.groapp.Models.NotificationModel
import com.example.groapp.R
import com.example.groapp.Utils.PseudoCookie
import com.google.firebase.database.*

class NotificationActivity : AppCompatActivity() {
    private lateinit var notificationRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var notificationList: ArrayList<NotificationModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        var backImg : ImageView = findViewById(R.id.backImg)
        backImg.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        var tvMark : ImageView = findViewById(R.id.tvMark)
        tvMark.setOnClickListener{
            deleteNotificationsForUserId(PseudoCookie.getPseudoCookie().getCookieValue("logged_user_id"))
        }

        notificationRecyclerView = findViewById(R.id.rvNotification)
        notificationRecyclerView.layoutManager = LinearLayoutManager(this)
        notificationRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        notificationList = arrayListOf<NotificationModel>()
        getNotificationsData(PseudoCookie.getPseudoCookie().getCookieValue("logged_user_id"))
    }

    private fun getNotificationsData(userId: String) {

        notificationRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("notifications")
        val query = dbRef.orderByChild("userId").equalTo(userId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notificationList.clear()
                if (snapshot.exists()){
                    for (notifSnap in snapshot.children){
                        val notData = notifSnap.getValue(NotificationModel::class.java)
                        notificationList.add(notData!!)
                    }
                    // Sort the notification list by timestamp in descending order
                    notificationList.sortByDescending { it.timestamp }

                    println("Sent notifications to frontend")

                    val mAdapter = NotificationAdapter(notificationList)
                    notificationRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : NotificationAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            // Handle item click event
                        }
                    })

                    notificationRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
    }

    private fun deleteNotificationsForUserId(userId: String) {
        val database = FirebaseDatabase.getInstance()
        val notificationsRef = database.getReference("notifications")
        notificationsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (notSnap in snapshot.children) {
                    notSnap.ref.removeValue()
                }
                val intent = Intent(this@NotificationActivity, NotificationActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}