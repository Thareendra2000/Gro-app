package com.example.groapp.Activities.MarketPlace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.Activities.Garden.MyGardensActivity
import com.example.groapp.Activities.HomeActivity
import com.example.groapp.Activities.MyProfileActivity
import com.example.groapp.Adapters.CategoryAdapter
import com.example.groapp.Models.CategoryModel
import com.example.groapp.R
import com.google.firebase.database.*
import java.util.Locale

class MarketPlaceActivity : AppCompatActivity() {
    private lateinit var catRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var catList: ArrayList<CategoryModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var search: androidx.appcompat.widget.SearchView
    private lateinit var filterMarketCategory: ArrayList<CategoryModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_place)

        filterMarketCategory = arrayListOf<CategoryModel>()
        catList = arrayListOf<CategoryModel>()


        var tvHome : LinearLayout = findViewById(R.id.tvHome)
        tvHome.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        var tvGardens : LinearLayout = findViewById(R.id.tvLeaderboard)
        tvGardens.setOnClickListener{
            val intent = Intent(this, MyGardensActivity::class.java)
            startActivity(intent)
        }
        var tvProfile : LinearLayout = findViewById(R.id.tvProfile)
        tvProfile.setOnClickListener{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }

        catRecyclerView = findViewById(R.id.rvCat)
        catRecyclerView.layoutManager = LinearLayoutManager(this)
        catRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)


        // search category
        search = findViewById(R.id.searchMarket)
        search.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filter(newText.trim())
                }
                return true
            }
        })

        getCategoryData()
    }

    private fun getCategoryData() {

        catRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("categories")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                catList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val data = empSnap.getValue(CategoryModel::class.java)
                        catList.add(data!!)
                    }
                    filterMarketCategory.addAll(catList)
                    val mAdapter = CategoryAdapter(filterMarketCategory)
                    catRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : CategoryAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(
                                this@MarketPlaceActivity,
                                MarketBrowseCategoryItemsActivity::class.java
                            )
                            intent.putExtra("catName", catList[position].name)
                            startActivity(intent)
                        }
                    })

                    catRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error", error.message)
            }

        })
    }

//    var id: String ?= null,
//    var name: String ?= null,
//    var description : String ?= null,
//    var image : String ?= null

    private fun filter(query: String) {
        filterMarketCategory.clear()
        for (cat in catList) {
            if (
                cat.id?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true ||
                cat.name?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true ||
                cat.description?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true ||
                cat.image?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true
            ) {
                filterMarketCategory.add(cat)
            }
        }
        catRecyclerView.adapter?.notifyDataSetChanged()
    }
}