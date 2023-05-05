package com.example.groapp.Repositories

import android.R
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.groapp.Models.CategoryModel
import com.google.firebase.database.*

class CategoryRespository (
    var activity : AppCompatActivity,
    var categoriesRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("categories")
        ) {

    public fun getAllCategoriesForSpinner(categoryBox : Spinner, callback : (Boolean) -> Unit){
        var categories = mutableListOf<String>()
        Log.i("1", "Testinggggg")
        Log.i("2", "Testinggggg")
        Log.i("3", "Testinggggg")
        Log.i("4", "Testinggggg")
        Log.i("5", "Testinggggg")
        categoriesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categories.clear()
                Log.i("6", "Testinggggg")

                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        Log.i("7", "Testinggggg")
                        val catData = snap.getValue(CategoryModel::class.java)
                        categories.add(catData!!.name!!)
                    }
                }

                ArrayAdapter(
                    activity,
                    R.layout.simple_spinner_item,
                    categories
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    categoryBox.adapter = adapter
                }
                callback(true)
                Log.i("8", "Testinggggg")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Error", error.message)
                callback(false)
            }
        })
        Log.i("9", "Testinggggg")

    }
}