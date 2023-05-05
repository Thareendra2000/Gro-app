package com.example.groapp.Activities.Product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.groapp.Models.CategoryModel
import com.example.groapp.Models.ProductModel
import com.example.groapp.R
import com.example.groapp.Utils.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class AddItemActivity : AppCompatActivity() {
    private lateinit var categoryBox : Spinner;
    private lateinit var productNameBox : EditText;
    private lateinit var unitBox : EditText;
    private lateinit var unitPriceBox : EditText;
    private lateinit var bestBeforeBox: EditText;
    private lateinit var descriptionBox : EditText;
    private lateinit var quantityBox : EditText;
    private lateinit var addItemBtn : Button;

    private var category : String = "";
    private var productName : String = "";
    private var unit : String = "";
    private var unitPrice : Double = 0.0;
    private var bestBefore : Date = Date();
    private var description : String = "";
    private var quantity : Double = 0.0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        categoryBox = findViewById(R.id.category)
        productNameBox = findViewById(R.id.productName)
        unitBox = findViewById(R.id.unit)
        unitPriceBox = findViewById(R.id.unitPrice)
        bestBeforeBox = findViewById(R.id.bestBefore)
        descriptionBox = findViewById(R.id.description)
        quantityBox = findViewById(R.id.quantity)
        addItemBtn = findViewById(R.id.addItemBtn)

        val dbRef = FirebaseDatabase.getInstance().getReference("categories")
        var categories = mutableListOf<String>()

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categories.clear()

                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val catData = snap.getValue(CategoryModel::class.java)
                        categories.add(catData!!.name!!)
                    }
                }

                Log.i("categories" , categories.toString())

                ArrayAdapter(
                    this@AddItemActivity,
                    android.R.layout.simple_spinner_item,
                    categories
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    categoryBox.adapter = adapter
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Error", error.message)
            }
        })

        addItemBtn.setOnClickListener {
            handleAddItemBtnClick()
        }

    }

    private fun handleAddItemBtnClick() {
//        category = categoryBox.selectedItem.toString()
//        Log.i("category" , "categoryBox.selectedItem.toString()")
//        productName = productNameBox.text!!.toString()
//        unit = unitBox.text!!.toString()
//        unitPrice = unitPriceBox.text!!.toString() as Double
//        bestBefore = bestBeforeBox.text!!.toString() as Date
//        description = descriptionBox.text!!.toString()
//        quantity = quantityBox.text!!.toString() as Double

        var productInfo : ProductModel = ProductModel(
            null,
            "Chandler's garden",
            "-NUe1DcgFG32cZ7eOELX",
            "Fruits",
            "Homegrown countryside beans",
            Date(),
            "Beans",
            12.0,
            "Kilogram",
            120.0,
        )
        println(productInfo.production_id)
        println(productInfo.name)
        println(productInfo.category)
        println(productInfo.description)
        println(productInfo.best_before)
        println(productInfo.quantity)
        println(productInfo.unit)
        println(productInfo.unit_price)

        var productsRef = FirebaseDatabase.getInstance().reference.child("products")
        val productId = productsRef.push().key!!
        val response = Response(productId, false, "")
        var productName : List<String>;

        productInfo.production_id = productId
        productsRef.child(productId).setValue(productInfo)
            .addOnSuccessListener {
                Log.i("Success" , "Product ${productInfo.name} created")
                response.result = true
                response.message = "Product created successfully"
                Toast.makeText(this@AddItemActivity, "Product created successfully", Toast.LENGTH_LONG).show()
//                callback(response)
            }
            .addOnFailureListener { err ->
                Log.w("Error" , err.message.toString())
                response.message = err.localizedMessage
//                callback(response)
                Toast.makeText(this@AddItemActivity, "Product creation failed", Toast.LENGTH_LONG).show()
            }

        Toast.makeText(this@AddItemActivity, "End of btn handling", Toast.LENGTH_LONG).show()
    }
}