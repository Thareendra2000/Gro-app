package com.example.groapp.Activities.Garden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.groapp.Activities.HomeActivity
import com.example.groapp.Activities.MarketPlace.MarketPlaceActivity
import com.example.groapp.Activities.MyProfileActivity
import com.example.groapp.Models.GardenModel
import com.example.groapp.R
import com.example.groapp.Services.UserSingleton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddGardenActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var address:EditText
    private lateinit var phoneNo:EditText
    private lateinit var location:EditText
    private lateinit var description:EditText
    private lateinit var area:EditText
    private  lateinit var submitBtn: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_garden)

        name = findViewById(R.id.gardenName)
        address = findViewById(R.id.gardenAddress)
        phoneNo = findViewById(R.id.gardenPhoneNo)
        location = findViewById(R.id.gardenLocation)
        area = findViewById(R.id.gardenArea)
        description = findViewById(R.id.gardenDescription)
        submitBtn = findViewById(R.id.gardenSubmitBtn)

        dbRef = FirebaseDatabase.getInstance().getReference("Garden")

        submitBtn.setOnClickListener{
            saveGardenData()
        }

    }

    private fun saveGardenData() {
        //getting values
        val gardenName = name.text.toString()
        val gardenAddress = address.text.toString()
        val gardenArea = area.text.toString()
        val gardenPhoneNo = phoneNo.text.toString()
        val gardenDescription = description.text.toString()
        val gardenLocation = location.text.toString()

        var isValid = true

        if (gardenName.isEmpty()) {
            name.error = "Please enter the garden name"
            isValid = false
        }
        else if (!gardenName.matches("[a-zA-Z]+".toRegex())) {
            name.error = "Garden name can only contain letters"
        }
        if (gardenAddress.isEmpty()) {
            address.error = "Please enter address"
            isValid = false
        }
        if (gardenArea.isEmpty()) {
            area.error = "Please enter area"
            isValid = false
        }
        if (gardenLocation.isEmpty()) {
            location.error = "Please enter the location URL"
            isValid = false
        }
        if (gardenPhoneNo.isEmpty()) {
            phoneNo.error = "Please enter phone No"
            isValid = false
        } else if (!gardenPhoneNo.matches(Regex("[0-9]+"))) {
            phoneNo.error = "Phone number cannot contain characters"
            isValid = false
        }else if (gardenPhoneNo.length != 10) {
            phoneNo.error = "Phone number must be 10 digits"
            isValid = false
        }

        if (isValid) {
            val gardenId = dbRef.push().key!!
            val userId = UserSingleton.uid

            val garden = GardenModel(userId,gardenId,gardenName,gardenAddress,gardenPhoneNo,gardenLocation,gardenArea,gardenDescription)


            dbRef.child(gardenId).setValue(garden)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "Please correct the errors and try again", Toast.LENGTH_LONG).show()
        }
        //nav bar intents
        var accountBtn : LinearLayout = findViewById(R.id.tvProfile)
        accountBtn.setOnClickListener{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }
        var tvMarketPlace : LinearLayout = findViewById(R.id.tvMarketPlace)
        tvMarketPlace.setOnClickListener{
            val intent = Intent(this, MarketPlaceActivity::class.java)
            startActivity(intent)
        }
        var gardens : LinearLayout = findViewById(R.id.tvLeaderboard)
        gardens.setOnClickListener {
            val intent = Intent(this, GardenListActivity::class.java)
            startActivity(intent)
        }
        var home : LinearLayout = findViewById(R.id.tvHome)
        home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }


}



