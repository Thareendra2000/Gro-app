package com.example.tute5.volunteering.services

import android.util.Log
import com.example.tute5.volunteering.models.VolunteeringModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class volHours {

    companion object {
        fun calTotalUserVolHours(uid: String?, callback: (Int?) -> Unit) {

            val database = Firebase.database.reference

            val userQuery = database.child("Volunteering").orderByChild("userId").equalTo(uid)

            var totalHours = 0
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (recordSnapshot in dataSnapshot.children) {
                        val record = recordSnapshot.getValue(VolunteeringModel::class.java)
                        totalHours += record?.hours?.toInt() ?: 0
                    }
                    Log.d("TAG", "Total hours for user $uid: $totalHours")
                    callback(totalHours)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("TAG", "Error getting volunteer data", databaseError.toException())
                    callback(null)
                }
            }

            userQuery.addValueEventListener(valueEventListener)
        }
    }

}