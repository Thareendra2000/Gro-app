package com.example.groapp.Activities.Garden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.groapp.R
import com.google.firebase.database.*

class GardenDashboardActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var gardensCountTextView: TextView
//    private lateinit var volunteersCountTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garden_dashboard)

    database = FirebaseDatabase.getInstance().reference
    gardensCountTextView = findViewById(R.id.Amount)
//    volunteersCountTextView = findViewById(R.id.volunteers_count_text_view)

    database.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val gardensCount = dataSnapshot.child("Garden").childrenCount
//            val volunteersCount = dataSnapshot.child("volunteers").childrenCount

            gardensCountTextView.text = gardensCount.toString()
//            volunteersCountTextView.text = volunteersCount.toString()
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle errors
        }
    })

    }
}