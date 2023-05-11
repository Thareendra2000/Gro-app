package com.example.groapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.groapp.Activities.Cart.CartPendingActivity
import com.example.groapp.Activities.Garden.GardenListActivity
import com.example.groapp.Activities.MarketPlace.MarketPlaceActivity
import com.example.groapp.Activities.Volunteer.VolunteerMain
import com.example.groapp.R
import com.example.groapp.Services.UserSingleton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val user = Firebase.auth.currentUser
        user?.let {
            UserSingleton.name = it.displayName
            UserSingleton.email = it.email
            UserSingleton.uid = it.uid
        }

        var tvCart : ImageView = findViewById(R.id.tvCart)
        tvCart.setOnClickListener{
            val intent = Intent(this, CartPendingActivity::class.java)
            startActivity(intent)
        }

        var tvNotification : ImageView = findViewById(R.id.tvNotification)
        tvNotification.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }



        var volunteerfun : Button = findViewById(R.id.volunteer)
        volunteerfun.setOnClickListener {
            val intent = Intent(this, VolunteerMain::class.java)
            startActivity(intent)
        }
        var signout : Button = findViewById(R.id.signout)
        signout.setOnClickListener {
            Firebase.auth.signOut()
        }
        var profile : Button = findViewById(R.id.profile)
        profile.setOnClickListener {
            val intent = Intent(this, ProfileMain::class.java)
            startActivity(intent)
        }

        //Bottom nav bar intents
        var accountBtn : LinearLayout = findViewById(R.id.tvProfile)
        accountBtn.setOnClickListener{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }
        var gardenList : LinearLayout = findViewById(R.id.tvLeaderboard)
        gardenList.setOnClickListener {
            val intent = Intent(this, GardenListActivity::class.java)
            startActivity(intent)
        }
        var tvMarketPlace : LinearLayout = findViewById(R.id.tvMarketPlace)
        tvMarketPlace.setOnClickListener{
            val intent = Intent(this, MarketPlaceActivity::class.java)
            startActivity(intent)
        }
        var homeBtn : LinearLayout = findViewById(R.id.tvHome)
        homeBtn.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}