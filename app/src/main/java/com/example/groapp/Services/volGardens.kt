package com.example.groapp.Services

import android.util.Log
import com.example.groapp.Models.VolunteeringModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class volGardens {
    companion object {
        fun calTotalUserVolGardens(uid: String?, callback: (Int?) -> Unit) {
            val userId = uid;
            val uniqueGardens = HashSet<String>()

            val database = Firebase.database.reference
            val userQuery = database.child("Volunteering").orderByChild("userId").equalTo(userId)

            userQuery.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (recordSnapshot in dataSnapshot.children) {
                        val record = recordSnapshot.getValue(VolunteeringModel::class.java)
                        if (record != null) {
                            record.gardenId?.let { uniqueGardens.add(it) }
                        }
                    }
                    val numUniqueGardens = uniqueGardens.size
                    Log.d("TAG", "User $userId has volunteered at $numUniqueGardens unique gardens")
                    callback(numUniqueGardens)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("TAG", "Error getting volunteer data", databaseError.toException())
                }
            })
        }

    }
}