package com.example.groapp.Activities.Volunteer

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.groapp.Services.UserSingleton
import com.google.firebase.database.FirebaseDatabase
import com.example.groapp.Models.VolunteeringModel
import com.example.groapp.R
import java.text.SimpleDateFormat
import java.util.*

class VolunteeringDetailsActivity : AppCompatActivity() {

    private lateinit var tvHours: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvVolId: TextView
    private lateinit var tvGarden: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var calendar: Calendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteering_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("volId").toString(),
                intent.getStringExtra("volName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("volId").toString()
            )
        }


    }

    private fun initView() {
        tvVolId = findViewById(R.id.tvVolId)
        tvHours = findViewById(R.id.tvHours)
        tvDate = findViewById(R.id.tvDate)
        tvGarden = findViewById(R.id.tvGarden)
        tvStatus = findViewById(R.id.tvStatus)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvVolId.text = intent.getStringExtra("volId")
        tvHours.text = intent.getStringExtra("hours")
        tvDate.text = intent.getStringExtra("date")
        tvGarden.text = intent.getStringExtra("garden")
        tvStatus.text = intent.getStringExtra("status")
    }

//    private fun deleteRecord(
//        id: String
//    ) {
//        val dbRef = FirebaseDatabase.getInstance().getReference("Volunteering").child(id)
//        val mTask = dbRef.removeValue()
//
//        mTask.addOnSuccessListener {
//            Toast.makeText(this, "Volunteering Deleted", Toast.LENGTH_LONG).show()
//
//            val intent = Intent(this, FetchingActivity::class.java)
//            finish()
//            startActivity(intent)
//        }.addOnFailureListener { error ->
//            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
//        }
//    }

    private fun deleteRecord(id: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this volunteering record?")
        builder.setPositiveButton("Yes") { _, _ ->
            val dbRef = FirebaseDatabase.getInstance().getReference("Volunteering").child(id)
            val mTask = dbRef.removeValue()

            mTask.addOnSuccessListener {
                Toast.makeText(this, "Volunteering Deleted", Toast.LENGTH_LONG).show()

                val intent = Intent(this, FetchingActivity::class.java)
                finish()
                startActivity(intent)
            }.addOnFailureListener { error ->
                Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
            }
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.show()
    }

    private fun openUpdateDialog(
        volId: String,
        empName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.volunteering_update_dialog, null)

        mDialog.setView(mDialogView)

        val etHours = mDialogView.findViewById<EditText>(R.id.etHours)
        val etDate = mDialogView.findViewById<EditText>(R.id.etDate)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etHours.setText(intent.getStringExtra("hours").toString())
        etDate.setText(intent.getStringExtra("date").toString())


        mDialog.setTitle("Updating Volunteer Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        calendar = Calendar.getInstance()


         val startDateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

                etDate.setText(sdf.format(calendar.time))



            }
        //date pickers
        etDate.setOnClickListener {

            DatePickerDialog(this, startDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }



        btnUpdateData.setOnClickListener {
            updateEmpData(
                volId,
                etHours.text.toString(),
                etDate.text.toString(),
            )

            Toast.makeText(applicationContext, "Volunteering Record Updated!", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvHours.text = etHours.text.toString()
            tvDate.text = etDate.text.toString()


            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        volId: String,
        hours: String,
        date: String,

    ) {

         var userId = UserSingleton.uid
         var userName= UserSingleton.name
         var gardenId: String = "101"
         var gardenName: String = "Suwapetha"
         var status : String = "Pending"

        val dbRef = FirebaseDatabase.getInstance().getReference("Volunteering").child(volId)
        val volInfo = VolunteeringModel(volId,userId,userName, hours, gardenId,gardenName,date,status)
        dbRef.setValue(volInfo)
    }



}