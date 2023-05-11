package com.example.groapp.Activities.Garden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.Adapters.GardenAdapter
import com.example.groapp.Models.GardenModel
import com.example.groapp.R
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class MyGardensActivity : AppCompatActivity() {

    private lateinit var gardenRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var gardenList: ArrayList<GardenModel>
    private lateinit var filteredGardenList: ArrayList<GardenModel>
    private lateinit var dbRef: DatabaseReference

    private lateinit var searchView: androidx.appcompat.widget.SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_gardens)

        gardenRecyclerView = findViewById(R.id.rvMyGarden)
        gardenRecyclerView.layoutManager = LinearLayoutManager(this)
        gardenRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvMyGardensLoadingData)

        gardenList = arrayListOf<GardenModel>()
        filteredGardenList = arrayListOf<GardenModel>()

        val plusBtn = findViewById<View>(R.id.plusBtn)
        plusBtn.setOnClickListener {
            val intent = Intent(this, AddGardenActivity::class.java)
            startActivity(intent)
        }

        searchView = findViewById(R.id.search)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
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

        getGardensData()
    }

    private fun getGardensData() {

        gardenRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Garden")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                gardenList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(GardenModel::class.java)
                        gardenList.add(empData!!)
                    }
                    filteredGardenList.addAll(gardenList)
                    val mAdapter = GardenAdapter(filteredGardenList)
                    gardenRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : GardenAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@MyGardensActivity,  GardenDetailsOwnerViewActivity::class.java)

                            //put extras
                            intent.putExtra("gardenId", filteredGardenList[position].gardenId)
                            intent.putExtra("name", filteredGardenList[position].name)
                            intent.putExtra("area", filteredGardenList[position].area)
                            intent.putExtra("address", filteredGardenList[position].address)
                            intent.putExtra("description", filteredGardenList[position].description)
                            intent.putExtra("phoneNo", filteredGardenList[position].phoneNo)
                            startActivity(intent)
                        }

                    })

                    gardenRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun filter(query: String) {
        filteredGardenList.clear()
        for (garden in gardenList) {
            if (garden.name?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true) {
                filteredGardenList.add(garden)
            }
        }
        gardenRecyclerView.adapter?.notifyDataSetChanged()
    }

}