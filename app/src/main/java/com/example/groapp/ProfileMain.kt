package com.example.groapp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.groapp.volunteering.models.VolunteeringModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase



class ProfileMain : AppCompatActivity() {

    private lateinit var userName: TextView
    private lateinit var btnUpdate: Button
    private lateinit var userEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)

        btnUpdate = findViewById(R.id.btnUpdate)
        userName = findViewById(R.id.userName)
        userEmail = findViewById(R.id.userEmail)

        btnUpdate.setOnClickListener {
            openUpdateDialog() }



    if ( UserSingleton.name != null){
        btnUpdate.visibility = View.GONE
    }

            userName.text = UserSingleton.name;
            userEmail.text = UserSingleton.email;
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


