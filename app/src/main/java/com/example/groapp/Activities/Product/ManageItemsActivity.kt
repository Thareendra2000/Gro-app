package com.example.groapp.Activities.Product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.Activities.Garden.GardenDetailsOwnerViewActivity
import com.example.groapp.Adapters.ProductAdapter
import com.example.groapp.Models.ProductModel
import com.example.groapp.R
import com.example.groapp.Repositories.CategoryRespository
import com.example.groapp.Repositories.ProductRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageItemsActivity : AppCompatActivity() {
    private lateinit var gardenName : String
    private lateinit var gardenId : String

    private lateinit var titleTv : TextView
    private lateinit var categoriesSpinner : Spinner

    val categoryRespository = CategoryRespository(this)

    private lateinit var addItemBtn : LinearLayout
    private lateinit var backBtn : ImageView
    private lateinit var itemsRV : RecyclerView
    private lateinit var tvLoadingData : TextView
    private lateinit var productsList : ArrayList<ProductModel>

    val productRepository = ProductRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_items)

        gardenName = intent.getStringExtra("name").toString()
        gardenId = intent.getStringExtra("gardenId").toString()

        titleTv = findViewById(R.id.Title)
        titleTv.setText(gardenName)
        categoriesSpinner = findViewById(R.id.categories)

        Log.i("Garden ID", gardenId)
        Log.i("Garden Name", gardenName)
        categoryRespository.getAllCategoriesForSpinner(categoriesSpinner){ result ->
            if(result){
                itemsRV = findViewById(R.id.itemsRV)
                itemsRV.layoutManager = LinearLayoutManager(this)
                itemsRV.setHasFixedSize(true)
                tvLoadingData = findViewById(R.id.tvLoadingData)

                itemsRV.visibility = View.GONE
                tvLoadingData.visibility = View.VISIBLE
                getProducts()
            }
        }

        val gardenNameT = intent.getStringExtra("name")
        val gardenIdT = intent.getStringExtra("gardenId")
        val area = intent.getStringExtra("area")
        val address = intent.getStringExtra("address")
        val description = intent.getStringExtra("description")
        val phoneNo = intent.getStringExtra("phoneNo")
        backBtn = findViewById(R.id.backImg)
        backBtn.setOnClickListener{
            val intent = Intent(this, GardenDetailsOwnerViewActivity::class.java)
            intent.putExtra("name", gardenNameT)
            intent.putExtra("gardenId", gardenIdT)
            intent.putExtra("area", area)
            intent.putExtra("address", address)
            intent.putExtra("description", description)
            intent.putExtra("phoneNo", phoneNo)
            startActivity(intent)
        }

        addItemBtn = findViewById(R.id.addItemBtn)
        addItemBtn.setOnClickListener{
            val intent = Intent(this, AddItemActivity::class.java)
            intent.putExtra("gardenName", gardenName)
            intent.putExtra("gardenId", gardenId)
            startActivity(intent)
            finish()
        }

    }

    private fun getProducts() {
        itemsRV.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        var dbRef = FirebaseDatabase.getInstance().reference.child("products")

        productsList = arrayListOf<ProductModel>()
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productsList.clear()
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val prodData = snap.getValue(ProductModel::class.java)
                        if(prodData!!.garden_id == gardenId)
                            productsList.add(prodData!!)
                    }
                    val mAdapter = ProductAdapter(productsList)
                    itemsRV.adapter = mAdapter


                    itemsRV.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

//    private fun getProducts() {
//        productsList = arrayListOf<ProductModel>()
//
//        val productsListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                try {
//                    if (dataSnapshot.exists()) {
//                        for (snap in dataSnapshot.children) {
//                            val productData = snap.getValue(ProductModel::class.java)
//                            if(productData!!.garden_id!!.equals(gardenId, ignoreCase = true)){
//                                productsList.add(productData!!)
//                                Log.i("product Found", productData.name.toString())
//                            }
//
//                            val mAdapter = ProductAdapter(productsList)
//                            itemsRV.adapter = mAdapter
//
//                            itemsRV.visibility = View.VISIBLE
//                            tvLoadingData.visibility = View.GONE
//                        }
//                    }
//                    Log.i("callback", productsList.size.toString())
//                } catch (ex: Exception) {
//                    Log.w("exception", ex.localizedMessage)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.w("loadPost:onCancelled", databaseError.toException())
//            }
//        }
//        FirebaseDatabase.getInstance().reference.child("products").addValueEventListener(productsListener);
//
//    }
}