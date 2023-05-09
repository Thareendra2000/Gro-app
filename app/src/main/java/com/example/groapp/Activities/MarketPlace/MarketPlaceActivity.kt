package com.example.groapp.Activities.MarketPlace

import android.content.Intent
import android.os.Bundle
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

class MarketPlaceActivity : AppCompatActivity() {
    private lateinit var catRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var catList: ArrayList<CategoryModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_place)

        var tvHome : LinearLayout = findViewById(R.id.tvHome)
        tvHome.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        var tvLeaderboard : LinearLayout = findViewById(R.id.tvLeaderboard)
        tvLeaderboard.setOnClickListener{
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

        catList = arrayListOf<CategoryModel>()

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
                        val catData = empSnap.getValue(CategoryModel::class.java)
                        catList.add(catData!!)
                    }
                    val mAdapter = CategoryAdapter(catList)
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
                TODO("Not yet implemented")
            }

        })
    }
}