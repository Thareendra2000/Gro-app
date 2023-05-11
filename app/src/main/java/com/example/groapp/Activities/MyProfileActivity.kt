package com.example.groapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.example.groapp.Activities.Garden.MyGardensActivity
import com.example.groapp.Activities.Order.ManageOrdersActivity
import com.example.groapp.Activities.Volunteer.FetchingActivity
import com.example.groapp.Activities.Volunteer.VolunteerDashboard
import com.example.groapp.R

private lateinit var volEfforts: TextView
private lateinit var volDashboard: TextView
class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        var myGardensBtn : LinearLayout = findViewById(R.id.myGardensBtn)
        myGardensBtn.setOnClickListener{
            val intent = Intent(this, MyGardensActivity::class.java)
            startActivity(intent)
        }

        var myOrdersBtn : LinearLayout = findViewById(R.id.myOrdersBtn)
        myOrdersBtn.setOnClickListener{
            val intent = Intent(this, ManageOrdersActivity::class.java)
            startActivity(intent)
        }

        var home : LinearLayout = findViewById(R.id.tvHome)
        home.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        volEfforts = findViewById(R.id.volEfforts)
        volEfforts.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }
        volDashboard = findViewById(R.id.volDashboard)
        volDashboard.setOnClickListener {
            val intent = Intent(this, VolunteerDashboard::class.java)
            startActivity(intent)
        }
    }
}