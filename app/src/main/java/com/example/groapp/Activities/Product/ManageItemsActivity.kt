package com.example.groapp.Activities.Product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import android.widget.TextView
import com.example.groapp.Models.ProductModel
import com.example.groapp.R
import com.example.groapp.Repositories.CategoryRespository
import com.example.groapp.Repositories.ProductRepository

class ManageItemsActivity : AppCompatActivity() {
    private lateinit var gardenName : String
    private lateinit var gardenId : String
    private lateinit var products : List<ProductModel>

    private lateinit var titleTv : TextView
    private lateinit var categoriesSpinner : Spinner

    val categoryRespository = CategoryRespository(this)
    val productRespository = ProductRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_items)

        gardenName = intent.getStringExtra("gardenName").toString()
        gardenId = intent.getStringExtra("gardenId").toString()

        titleTv = findViewById(R.id.Title)
        titleTv.setText(gardenId)
        categoriesSpinner = findViewById(R.id.categories)

        Log.i("Garden ID", gardenId)
        Log.i("Garden Name", gardenName)
        categoryRespository.getAllCategoriesForSpinner(categoriesSpinner){ result ->
            if(result){
                productRespository.getAllProductsByGardenId(gardenId){ productsList ->
                    products = productsList
                }
            }
        }

    }
}