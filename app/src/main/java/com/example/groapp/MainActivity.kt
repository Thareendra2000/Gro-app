package com.example.groapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.groapp.volunteering.activities.VolunteerMain
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var volunteerfun: Button
    private lateinit var signout: Button
    private lateinit var profile: Button
    private lateinit var tvUserEmail: TextView
    private lateinit var tvUserUUI: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = Firebase.auth.currentUser
        user?.let {
            UserSingleton.name = it.displayName
            UserSingleton.email = it.email
            UserSingleton.uid = it.uid
        }

        tvUserEmail = findViewById(R.id.tvUserEmail)
        tvUserUUI = findViewById(R.id.tvUserUUI)
        volunteerfun = findViewById(R.id.volunteer)
        signout = findViewById(R.id.signout)
        profile = findViewById(R.id.profile)

        tvUserEmail.text = UserSingleton.email
        tvUserUUI.text = UserSingleton.uid


        volunteerfun.setOnClickListener {
            val intent = Intent(this, VolunteerMain::class.java)
            startActivity(intent)
        }

        signout.setOnClickListener {
            Firebase.auth.signOut()
        }

        profile.setOnClickListener {
            val intent = Intent(this, ProfileMain::class.java)
            startActivity(intent)
        }


    }
}