package com.example.groapp.Activities.Product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.groapp.Activities.Garden.MyGardensActivity
import com.example.groapp.R

class EditDeleteItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_delete_item)

        var backBtn : ImageView = findViewById(R.id.backImg)
        backBtn.setOnClickListener{
            val intent = Intent(this, ManageItemsActivity::class.java)
            startActivity(intent)
        }
    }
}