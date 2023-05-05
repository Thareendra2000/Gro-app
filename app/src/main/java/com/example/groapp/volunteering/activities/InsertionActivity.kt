package com.example.tute5.volunteering.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tute5.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.tute5.volunteering.models.VolunteeringModel

class InsertionActivity : AppCompatActivity() {

    private var userId: String = "001"
    private var userName: String = "Dinal"
    private var gardenId: String = "101"
    private var gardenName: String = "Suwapetha"

    private lateinit var etHours: EditText
    private lateinit var etDate: EditText

    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.volunteer_form)



        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Volunteer Record"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)




        etHours = findViewById(R.id.etHours)
        etDate = findViewById(R.id.etDate)

        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Volunteering")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun saveEmployeeData() {

        //getting values
        val hours = etHours.text.toString()
        val date = etDate.text.toString()
//        val empSalary = etEmpSalary.text.toString()

        if (hours.isEmpty()) {
            etHours.error = "Please Enter the Amount of Hours"
            return
        }
        if (date.isEmpty()) {
            etDate.error = "Please Enter the Date"
            return
        }

        val volunteeringId = dbRef.push().key!!

        val employee = VolunteeringModel(volunteeringId,userId,userName, hours, gardenId,gardenName,date)

        dbRef.child(volunteeringId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Volunteering Record Added!", Toast.LENGTH_LONG).show()

                etHours.text.clear()
                etDate.text.clear()

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }

}