package com.example.groapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.groapp.Activities.Garden.MyGardensActivity
import com.example.groapp.Activities.Order.ManageOrdersActivity
import com.example.groapp.R

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
        var marketplace : LinearLayout = findViewById(R.id.tvMarketPlace)
        marketplace.setOnClickListener{
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
    }
}