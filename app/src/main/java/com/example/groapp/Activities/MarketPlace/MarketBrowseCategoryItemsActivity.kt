package com.example.groapp.Activities.MarketPlace

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.Activities.Garden.GardenListActivity
import com.example.groapp.Activities.Garden.MyGardensActivity
import com.example.groapp.Activities.MyProfileActivity
import com.example.groapp.R
import com.example.groapp.Adapters.BrowseCategoryItemAdapter
import com.example.groapp.Models.ProductModel
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class MarketBrowseCategoryItemsActivity : AppCompatActivity() {
    private lateinit var browseItemRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit var dbRef: DatabaseReference

    private lateinit var backImg: ImageView
    private lateinit var activityIntent: Intent
    private lateinit var catNameTextView: TextView
    private lateinit var catName: String
    private lateinit var resultBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_browse_category_items)

        backImg = findViewById(R.id.backImg)
        backImg.setOnClickListener {
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

        resultBtn = findViewById(R.id.resultsCount)

        activityIntent = intent
        catName = activityIntent.getStringExtra("catName").toString()
        catNameTextView = findViewById(R.id.catNameTitle)
        catNameTextView.text = catName

        browseItemRecyclerView = findViewById(R.id.rvBrowseItem)
        browseItemRecyclerView.layoutManager = LinearLayoutManager(this)
        browseItemRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        productList = arrayListOf<ProductModel>()

        getProductsData()

    }

    private fun getProductsData() {
        browseItemRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("products")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                if (snapshot.exists()) {
                    for (prodSnap in snapshot.children) {
                        val prodData = prodSnap.getValue(ProductModel::class.java)
                        if (prodData?.category == catName) {
                            productList.add(prodData!!)
                        }
                    }

                    val mAdapter = BrowseCategoryItemAdapter(productList)
                    browseItemRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object :
                        BrowseCategoryItemAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(
                                this@MarketBrowseCategoryItemsActivity,
                                MarketPlaceAddItemToCartActivity::class.java
                            )

                            intent.putExtra("prodId", productList[position].production_id)
                            intent.putExtra("gardenName", productList[position].garden_name)
                            intent.putExtra("gardenId", productList[position].garden_id)
                            intent.putExtra("category", productList[position].category)
                            intent.putExtra("description", productList[position].description)
                            intent.putExtra("bestBefore", productList[position].best_before)
                            intent.putExtra("name", productList[position].name)
                            intent.putExtra("quantity", productList[position].quantity)
                            intent.putExtra("unit", productList[position].unit)
                            intent.putExtra("unitPrice", productList[position].unit_price)
                            intent.putExtra("image_url", productList[position].img_url)
                            intent.putExtra("rating", productList[position].rating)

                            startActivity(intent)
                        }

                    })

                    resultBtn.text = "${productList.size} items"
                    browseItemRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print("Something went wrong")
            }

        })
    }
}