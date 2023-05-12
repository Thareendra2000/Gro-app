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
import com.example.groapp.Activities.Garden.GardenListActivity
import com.example.groapp.Activities.Garden.MyGardensActivity
import com.example.groapp.Activities.HomeActivity
import com.example.groapp.Activities.MarketPlace.MarketPlaceActivity
import com.example.groapp.Activities.MyProfileActivity
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
//    private lateinit var categoriesSpinner : Spinner

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

        var home : LinearLayout = findViewById(R.id.tvHome)
        home.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        var marketplace : LinearLayout = findViewById(R.id.tvMarketPlace)
        marketplace.setOnClickListener{
            val intent = Intent(this, MarketPlaceActivity::class.java)
            startActivity(intent)
        }
        var tvGardens : LinearLayout = findViewById(R.id.tvLeaderboard)
        tvGardens.setOnClickListener{
            val intent = Intent(this, GardenListActivity::class.java)
            startActivity(intent)
        }
        var tvProfile : LinearLayout = findViewById(R.id.tvProfile)
        tvProfile.setOnClickListener{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }
        gardenName = intent.getStringExtra("name").toString()
        gardenId = intent.getStringExtra("gardenId").toString()

        titleTv = findViewById(R.id.Title)
        titleTv.setText(gardenName)
//        categoriesSpinner = findViewById(R.id.categories)
        itemsRV = findViewById(R.id.itemsRV)
        itemsRV.layoutManager = LinearLayoutManager(this)
        itemsRV.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)


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
            intent.putExtra("name", gardenName)
            intent.putExtra("gardenId", gardenId)
            startActivity(intent)
        }

        getProducts()
    }


    private fun getProducts() {

        itemsRV.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        var dbRef = FirebaseDatabase.getInstance().getReference("products")

        var empList = arrayListOf<ProductModel>()
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val empData = empSnap.getValue(ProductModel::class.java)
                        if(empData!!.garden_id.toString() == gardenId.toString())
                            empList.add(empData!!)
                    }

                    if(empList.size > 0)
                    {
                        val mAdapter = ProductAdapter(empList)
                        itemsRV.adapter = mAdapter

                        mAdapter.setOnItemClickListener(object : ProductAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                            }

                        })

                        itemsRV.visibility = View.VISIBLE
                        tvLoadingData.visibility = View.GONE
                    }
                    else{
                        tvLoadingData.text = " No Products Found";
                    }
                }
                else{
                    tvLoadingData.text = " No Products Found";
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}