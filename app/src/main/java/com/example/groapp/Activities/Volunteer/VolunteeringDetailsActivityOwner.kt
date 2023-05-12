package com.example.groapp.Activities.Volunteer

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.groapp.Services.UserSingleton
import com.google.firebase.database.FirebaseDatabase
import com.example.groapp.Models.VolunteeringModel
import com.example.groapp.R

class VolunteeringDetailsActivityOwner : AppCompatActivity() {

    private lateinit var tvHours: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvVolId: TextView
    private lateinit var tvGarden: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnApprove: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteering_details_owner)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Approve Volunteer Requests"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)


        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("volId").toString(),
                intent.getStringExtra("volName").toString()
            )
        }

        btnApprove.setOnClickListener {
            updateEmpData(
                tvVolId.text.toString(),
                tvHours.text.toString(),
                tvDate.text.toString(),
                intent.getStringExtra("gardenId").toString() ,
                intent.getStringExtra("garden").toString(),
                "Approved"
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
        btnApprove = findViewById(R.id.btnApprove)
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
        val mDialogView = inflater.inflate(R.layout.volunteering_update_dialog_owner, null)

        mDialog.setView(mDialogView)

        val etHours = mDialogView.findViewById<EditText>(R.id.etHours)
        val etDate = mDialogView.findViewById<EditText>(R.id.etDate)
        //val etStatus = mDialogView.findViewById<EditText>(R.id.etStatus)
        var hdStatus : String = "Pending"
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etHours.setText(intent.getStringExtra("hours").toString())
        etDate.setText(intent.getStringExtra("date").toString())
        var hdGardenId : String = intent.getStringExtra("gardenId").toString()
        var hdGardenName : String = intent.getStringExtra("garden").toString()
//        var Status = intent.getStringExtra("status").toString()
//        var indexStatus : Int = 0;
//        val spinner = findViewById<Spinner>(R.id.etStatus)
//
//        if ( Status == "Pending"){
//            indexStatus = 0
//        }else if (Status == "Approved"){
//            indexStatus = 1
//        } else if (Status == "Confirmed"){
//            indexStatus = 2
//        }
//        Log.d(TAG, "The index status is: $indexStatus")
//
//        spinner?.setSelection(indexStatus) // Set the initial selected item to the third item (index 2)
//        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                val selectedItem = parent.getItemAtPosition(position) as String
//                // Do something with the selected item
//                hdStatus = selectedItem
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Do nothing when nothing is selected
//            }
//        }

        mDialog.setTitle("Updating Volunteer Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                volId,
                etHours.text.toString(),
                etDate.text.toString(),
                hdGardenId,
                hdGardenName,
                "Confirmed"
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
        gardenId: String,
        gardenName: String,
        status:String
    ) {

         var userId = UserSingleton.uid
         var userName= UserSingleton.name


        val dbRef = FirebaseDatabase.getInstance().getReference("Volunteering").child(volId)
        val volInfo = VolunteeringModel(volId,userId,userName, hours, gardenId,gardenName,date,status)
        dbRef.setValue(volInfo)
    }

}