package com.example.tute5.volunteering.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.tute5.R
import androidx.appcompat.app.AppCompatActivity


class VolunteerMain  : AppCompatActivity() {
    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button
    private lateinit var btnFetchDataOwner: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.volunteer_main)


        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)
        btnFetchDataOwner = findViewById(R.id.btnFetchDataOwner)

        btnInsertData.setOnClickListener {
            val intent = Intent(this, InsertionActivity::class.java)
            startActivity(intent)
        }

        btnFetchData.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }

        btnFetchDataOwner.setOnClickListener {
            val intent = Intent(this, FetchingActivityOwner::class.java)
            startActivity(intent)
        }

    }
}