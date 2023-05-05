package com.example.groapp.Activities.Garden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.Adapters.GardenAdapter
import com.example.groapp.Models.GardenModel
import com.example.groapp.R
import com.google.firebase.database.*

class GardenListActivity : AppCompatActivity() {

    private lateinit var gardenRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var gardenList: ArrayList<GardenModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garden_list)

        gardenRecyclerView = findViewById(R.id.rvGarden)
        gardenRecyclerView.layoutManager = LinearLayoutManager(this)
        gardenRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        gardenList = arrayListOf<GardenModel>()

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
                    val mAdapter = GardenAdapter(gardenList)
                    gardenRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : GardenAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@GardenListActivity,  GardenDetailsVolunteerViewActivity::class.java)

                            //put extras
                            intent.putExtra("gardenId", gardenList[position].gardenId)
                            intent.putExtra("gardenName", gardenList[position].name)
                            intent.putExtra("gardenArea", gardenList[position].area)
                            intent.putExtra("gardenAddress", gardenList[position].address)
                            intent.putExtra("gardenDescription", gardenList[position].description)
                            intent.putExtra("gardenPhoneNo", gardenList[position].phoneNo)
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
}