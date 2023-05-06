package com.example.groapp.Activities.Garden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.groapp.Activities.HomeActivity
import com.example.groapp.Activities.LoginActivity
import com.example.groapp.R

class GardenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garden)


        val Btn3 = findViewById<View>(R.id.button4)
        Btn3.setOnClickListener {
            val intent = Intent(this, MyGardensActivity::class.java)
            startActivity(intent)
        }
        val Btn4 = findViewById<View>(R.id.button5)
        Btn4.setOnClickListener {
            val intent = Intent(this, GardenListActivity::class.java)
            startActivity(intent)
        }

        val Btn5 = findViewById<View>(R.id.button3)
        Btn5.setOnClickListener {
            val intent = Intent(this, GardenDashboardActivity::class.java)
            startActivity(intent)
        }
    }
}