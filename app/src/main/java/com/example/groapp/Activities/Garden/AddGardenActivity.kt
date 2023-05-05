package com.example.groapp.Activities.Garden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.groapp.Models.GardenModel
import com.example.groapp.R
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

    private fun saveGardenData(){
        //getting values
        val gardenName = name.text.toString()
        val gardenAddress = address.text.toString()
        val gardenArea = area.text.toString()
        val gardenPhoneNo = phoneNo.text.toString()
        val gardenDescription = description.text.toString()
        val gardenLocation = location.text.toString()

        if (gardenName.isEmpty()) {
            name.error = "Please enter the garden name"
        }
        if (gardenAddress.isEmpty()) {
            address.error = "Please enter address"
        }
        if (gardenArea.isEmpty()) {
            area.error = "Please enter area"
        }
        if (gardenLocation.isEmpty()) {
            location.error = "Please enter the location URL"
        }
        if (gardenPhoneNo.isEmpty()) {
            phoneNo.error = "Please enter phone No"
        }


        val gardenId = dbRef.push().key!!

        val garden = GardenModel(gardenId,gardenName,gardenAddress,gardenPhoneNo,gardenLocation,gardenArea,gardenDescription)

        dbRef.child(gardenId).setValue(garden)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()



            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }
}