package com.example.groapp.Activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.groapp.Activities.Garden.MyGardensActivity
import com.example.groapp.Activities.Order.ManageOrdersActivity
import com.example.groapp.Activities.Volunteer.FetchingActivity
import com.example.groapp.Activities.Volunteer.VolunteerDashboard
import com.example.groapp.R
import com.example.groapp.Services.UserSingleton
import com.example.groapp.Services.volHours
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

private lateinit var volEfforts: TextView
private lateinit var volDashboard: TextView
private lateinit var userName: TextView
private lateinit var btnUpdate: TextView
private lateinit var userEmail: TextView
class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

//        val user = FirebaseAuth.getInstance().currentUser
//        val newUsername = "Dinal"
//
//        val profileUpdates = UserProfileChangeRequest.Builder()
//            .setDisplayName(newUsername)
//            .build()

        btnUpdate = findViewById(R.id.btnUpdate)
        userName = findViewById(R.id.userName)
        userEmail = findViewById(R.id.userEmail)

        var myGardensBtn: LinearLayout = findViewById(R.id.myGardensBtn)
        myGardensBtn.setOnClickListener {
            val intent = Intent(this, MyGardensActivity::class.java)
            startActivity(intent)
        }

        var myOrdersBtn: LinearLayout = findViewById(R.id.myOrdersBtn)
        myOrdersBtn.setOnClickListener {
            val intent = Intent(this, ManageOrdersActivity::class.java)
            startActivity(intent)
        }

        var home: LinearLayout = findViewById(R.id.tvHome)
        home.setOnClickListener {
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

            var marketplace: LinearLayout = findViewById(R.id.tvMarketPlace)
            marketplace.setOnClickListener {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            var tvGardens: LinearLayout = findViewById(R.id.tvLeaderboard)
            tvGardens.setOnClickListener {
                val intent = Intent(this, MyGardensActivity::class.java)
                startActivity(intent)
            }
            var tvProfile: LinearLayout = findViewById(R.id.tvProfile)
            tvProfile.setOnClickListener {
                val intent = Intent(this, MyProfileActivity::class.java)
                startActivity(intent)
        }

//        if ( UserSingleton.name != null){
//            btnUpdate.visibility = View.VISIBLE
//        }

        userName.text = UserSingleton.name;
        userEmail.text = UserSingleton.email;

        btnUpdate.setOnClickListener {
            openUpdateDialog() }

    }

    private fun openUpdateDialog() {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.user_update_dialog, null)

        mDialog.setView(mDialogView)

        val etUserName = mDialogView.findViewById<EditText>(R.id.etUserName)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        mDialog.setTitle("Updating Display Name")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateUserName(etUserName.text.toString())

            Toast.makeText(applicationContext, "Display Name Updated!", Toast.LENGTH_LONG)
                .show()

            //we are setting updated data to our textviews
            userName.text = etUserName.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateUserName(userName: String, ) {
        val user = Firebase.auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = userName
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "User profile updated.")
                }
            }

    }
}