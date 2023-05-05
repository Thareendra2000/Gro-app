package com.example.groapp.Activities.Garden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.groapp.R

class GardenDetailsVolunteerViewActivity : AppCompatActivity() {
    // Declare variables for the UI elements.
    private lateinit var gardenName: TextView
    private lateinit var gardenAddress: TextView
    private lateinit var gardenArea: TextView
    private lateinit var gardenLocation: TextView
    private lateinit var gardenDescription: TextView
    private lateinit var gardenPhoneNo: TextView
    private lateinit var volunteerBtn: Button

    private var gardenId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garden_details_volunteer_view)


        initView()
        setValuesToViews()

        // Set a click listener on the volunteer button.
        volunteerBtn.setOnClickListener {
            // Create an intent to start the volunteer register activity.
            //val intent = Intent(this, VolunteerRegisterActivity::class.java) //put registration activity name here
            // Add the garden ID as an extra to the intent.
            intent.putExtra("gardenId", gardenId)
            // Start the activity with the intent.
            startActivity(intent)
        }

    }
    // Initialize the UI elements by finding them in the layout file.
    private fun initView() {

        gardenName = findViewById(R.id.gardenName)
        gardenAddress = findViewById(R.id.gardenAddress)
        gardenArea = findViewById(R.id.gardenArea)
        gardenLocation = findViewById(R.id.gardenLocation)
        gardenPhoneNo = findViewById(R.id.gardenPhoneNo)
        gardenDescription  = findViewById(R.id.gardenDescription)

        volunteerBtn = findViewById(R.id.volunteerBtn)
    }

    // Set the values of the UI elements with data passed from the previous activity.
    private fun setValuesToViews() {

        gardenId = intent.getIntExtra("gardenId", 0)
        gardenName.text = intent.getStringExtra("gardenName")
        gardenAddress.text = intent.getStringExtra("gardenAddress")
        gardenArea.text = intent.getStringExtra("gardenArea")
        gardenPhoneNo.text = intent.getStringExtra("gardenPhoneNo")
        gardenDescription.text = intent.getStringExtra("gardenDescription")
        gardenLocation.text = intent.getStringExtra("gardenLocation")



    }


}