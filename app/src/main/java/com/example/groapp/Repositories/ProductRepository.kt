package com.example.groapp.Repositories

import android.app.Activity
import android.app.ActivityManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.groapp.Models.ProductModel
import com.example.groapp.Utils.CastToObject
import com.example.groapp.Utils.Response
import com.google.firebase.database.*

class ProductRepository (
    var activity : AppCompatActivity,
    var productsRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("products")
)
{

    public fun createProduct(product: ProductModel){
        val productId = productsRef.push().key!!
        product.production_id = productId

        productsRef.child(productId).setValue(product)
            .addOnSuccessListener {
                Log.i("Success" , "Product ${product.name} created")
                Toast.makeText(activity, "Product created successfully", Toast.LENGTH_LONG).show()

            }
            .addOnFailureListener { err ->
                Log.w("Error" , err.message.toString())
                Toast.makeText(activity, "Product creation failed", Toast.LENGTH_LONG).show()
            }
    }

    public fun getAllProducts(callback: (List<ProductModel>) -> Unit) {
        var products = mutableListOf<ProductModel>()

        val productsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        for (snap in dataSnapshot.children) {
                            val productData = snap.getValue(ProductModel::class.java)
                            products.add(productData!!)
                        }
                    }
//                    if(dataSnapshot.value != null){
//                        val productsSnapshot = dataSnapshot.value as HashMap<String, HashMap<String, String>>
//                        for (product in productsSnapshot) {
//                            var productObj = ProductModel()
//
//                            for ((key, value) in product.value)
//                                productObj = ProductModel().toProduct(productObj, key, value)
//                            products.add(productObj)
//                        }
//                        callback(products)
//                    }
                    //Log.i("Products List", products.size.toString())

                    callback(products)
                } catch (ex: Exception) {
                    Log.w("exception", ex.localizedMessage)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        }
        productsRef.addValueEventListener(productsListener);

    }

    public fun getProduct(productId: String){

    }

    public fun updateProduct(){

    }
}