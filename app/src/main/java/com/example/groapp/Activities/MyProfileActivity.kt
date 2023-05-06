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
    }
}