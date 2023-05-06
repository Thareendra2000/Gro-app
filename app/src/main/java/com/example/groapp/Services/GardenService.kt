package com.example.groapp.Services

import com.example.groapp.Models.GardenModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GardenService {
    fun getGardenById(gardenId: String, callback: (GardenModel?, String?) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("garden/-NUe22yNjXISVCQTOmzS")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val garden = snapshot.getValue(GardenModel::class.java)
                if (garden != null) {
                    callback(garden, null)
                } else {
                    callback(null, "Garden not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, error.message)
            }
        })
        // Add a debugging statement
        println("GardenService.getGardenById called with gardenId = $gardenId")
    }
}

