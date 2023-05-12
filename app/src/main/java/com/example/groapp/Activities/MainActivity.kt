package com.example.groapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.groapp.Activities.Volunteer.VolunteerMain
import com.example.groapp.R
import com.example.groapp.Services.UserSingleton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var volunteerfun: Button
    private lateinit var signout: Button
    private lateinit var profile: Button
    private lateinit var garden: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = Firebase.auth.currentUser
        user?.let {
            UserSingleton.name = it.displayName
            UserSingleton.email = it.email
            UserSingleton.uid = it.uid
        }


        volunteerfun = findViewById(R.id.volunteer)
        signout = findViewById(R.id.signout)
        profile = findViewById(R.id.profile)
        garden = findViewById(R.id.garden)




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

//        garden.setOnClickListener {
//            val intent = Intent(this, GardenActivity::class.java)
//            startActivity(intent)
//        }

    }
}