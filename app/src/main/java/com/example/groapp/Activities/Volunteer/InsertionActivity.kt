package com.example.groapp.Activities.Volunteer

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.groapp.Models.VolunteeringModel
import com.example.groapp.R
import com.example.groapp.Services.UserSingleton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.text.SimpleDateFormat
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.example.groapp.Utils.VolunteerValidations


class InsertionActivity : AppCompatActivity() {


    private var userId = UserSingleton.uid
    private var userName = UserSingleton.name

    private lateinit var gardenId : String
    private lateinit var gardenName : String

    private lateinit var calendar: Calendar

    private lateinit var etHours: EditText
    private lateinit var etDate: EditText


    private lateinit var etGardenName: TextView

    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference
    var volunteerValidations = VolunteerValidations()

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
        etGardenName = findViewById(R.id.etGardenName)
        etGardenName.text = intent.getStringExtra("gardenName")
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Volunteering")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }


        calendar = Calendar.getInstance()

        //date pickers
        etDate.setOnClickListener {

            DatePickerDialog(this, startDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
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

        gardenId = intent.getStringExtra("gardenId").toString()
        gardenName = intent.getStringExtra("gardenName").toString()

        if (hours.isEmpty()) {
            etHours.error = "Please Enter the Amount of Hours"
            return
        }
        if ( !volunteerValidations.isValidHours(hours)){
            etHours.error = "Please Enter a Valid Amount of Hours (1 - 7)"
            return
        }
        if (date.isEmpty()) {
            etDate.error = "Please Enter the Date"
            return
        }

        if ( !volunteerValidations.isValidYear(date)){
            etDate.error = "Please Enter a Valid Date ( 30 Days )"
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

    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

            etDate.setText(sdf.format(calendar.time))



        }
}