package com.example.groapp.Activities.Garden

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.StringDef
import com.example.groapp.Activities.MyProfileActivity
import com.example.groapp.Activities.Product.ManageItemsActivity
import com.example.groapp.Models.GardenModel
import com.example.groapp.R
import com.google.firebase.database.FirebaseDatabase

class GardenDetailsOwnerViewActivity : AppCompatActivity() {

    // Declare variables for the UI elements.
    private lateinit var gardenName: TextView
    private lateinit var gardenId: TextView
    private lateinit var gardenAddress: TextView
    private lateinit var gardenArea: TextView
    private lateinit var gardenLocation: TextView
    private lateinit var gardenDescription: TextView
    private lateinit var gardenPhoneNo: TextView
    private lateinit var productionAddBtn: Button
    private lateinit var editBtn: ImageButton
    private lateinit var deleteBtn: ImageButton




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garden_details_owner_view)

        initView()
        setValuesToViews()

        editBtn.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("gardenId").toString(),
                intent.getStringExtra("name").toString()
            )
        }

        deleteBtn.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("gardenId").toString()
            )
        }


        var viewProducts : Button = findViewById(R.id.viewProducts)
        viewProducts.setOnClickListener{
            val intent = Intent(this, ManageItemsActivity::class.java)
            intent.putExtra("gardenName", gardenName.text.toString())
            intent.putExtra("gardenId", intent.getStringExtra("gardenId").toString())
            startActivity(intent)
        }

        // Set a click listener on the volunteer button.

    }
    // Initialize the UI elements by finding them in the layout file.
    private fun initView() {
        gardenId = findViewById(R.id.gardenId)
        gardenName = findViewById(R.id.gardenName)
        gardenAddress = findViewById(R.id.gardenAddress)
        gardenArea = findViewById(R.id.gardenArea)
        gardenLocation = findViewById(R.id.gardenLocation)
        gardenPhoneNo = findViewById(R.id.gardenPhoneNo)
        gardenDescription  = findViewById(R.id.gardenDescription)

        productionAddBtn = findViewById(R.id.volunteerBtn)
        editBtn =findViewById(R.id.btnEdit)
        deleteBtn = findViewById(R.id.btnDelete)
    }

    // Set the values of the UI elements with data passed from the previous activity.
    private fun setValuesToViews() {

        gardenId.text = intent.getStringExtra("gardenId")
        gardenName.text = intent.getStringExtra("name")
        gardenAddress.text = intent.getStringExtra("address")
        gardenArea.text = intent.getStringExtra("area")
        gardenPhoneNo.text = intent.getStringExtra("phoneNo")
        gardenDescription.text = intent.getStringExtra("description")
        gardenLocation.text = intent.getStringExtra("location")

    }


    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Garden").child(id)

        val confirmDialog = AlertDialog.Builder(this)
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this record?")
            .setPositiveButton("Yes") { _, _ ->
                dbRef.removeValue().addOnSuccessListener {
                    Toast.makeText(this, "Garden data deleted", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, MyGardensActivity::class.java)
                    finish()
                    startActivity(intent)
                }.addOnFailureListener{ error ->
                    Toast.makeText(this, "Deleting Error ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("No", null)
            .create()

        confirmDialog.show()


    }

    private fun openUpdateDialog(
        gardenId: String,
        name: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.garden_update_dialog, null)

        mDialog.setView(mDialogView)

        val upGName = mDialogView.findViewById<EditText>(R.id.upGardenName)
        val upGAddress = mDialogView.findViewById<EditText>(R.id.upGardenAddress)
        val upGArea = mDialogView.findViewById<EditText>(R.id.upGardenArea)
        val upGLocation = mDialogView.findViewById<EditText>(R.id.upGardenLocation)
        val upGPhoneNo = mDialogView.findViewById<EditText>(R.id.upGardenPhoneNo)
        val upGDescription = mDialogView.findViewById<EditText>(R.id.upGardenDescription)


        val btnUpdateData = mDialogView.findViewById<Button>(R.id.gardenUpdateBtn)

        upGName.setText(intent.getStringExtra("name").toString())
        upGAddress.setText(intent.getStringExtra("address").toString())
        upGArea.setText(intent.getStringExtra("area").toString())
        upGLocation.setText(intent.getStringExtra("location").toString())
        upGPhoneNo.setText(intent.getStringExtra("phoneNo").toString())
        upGDescription.setText(intent.getStringExtra("description").toString())

        mDialog.setTitle("Updating $name Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                gardenId,
                upGName.text.toString(),
                upGAddress.text.toString(),
                upGArea.text.toString(),
                upGLocation.text.toString(),
                upGPhoneNo.text.toString(),
                upGDescription.text.toString()
            )

            Toast.makeText(applicationContext, "Garden Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            gardenName.text = upGName.text.toString()
            gardenAddress.text = upGAddress.text.toString()
            gardenArea.text = upGArea.text.toString()
            gardenPhoneNo.text = upGPhoneNo.text.toString()
            gardenDescription.text = upGDescription.text.toString()
            gardenLocation.text = upGLocation.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        name: String,
        address: String,
        area: String,
        location: String,
        phoneNo:String,
        description:String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Garden").child(id)
        val gardenInfo = GardenModel(id, name, address, area,location,phoneNo,description)
        dbRef.setValue(gardenInfo)
    }
}