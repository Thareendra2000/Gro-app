package com.example.groapp.Activities.Volunteer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tute5.volunteering.adapters.VolAdapter
import com.google.firebase.database.*
import com.example.tute5.volunteering.models.VolunteeringModel

class FetchingActivityOwner : AppCompatActivity() {

    private lateinit var volRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var volList: ArrayList<VolunteeringModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.volunteering_activity_fetching)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Garden Volunteer Records"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)


        volRecyclerView = findViewById(R.id.rvVol)
        volRecyclerView.layoutManager = LinearLayoutManager(this)
        volRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        volList = arrayListOf<VolunteeringModel>()

        getEmployeesData()

    }

    private fun getEmployeesData() {

        volRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE


//        val dbRef = FirebaseDatabase.getInstance().getReference("Employees")
//        val query = dbRef.orderByChild("empAge").equalTo(25)
//
//        query.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // iterate over the snapshot to get the matching employees
//                for (empSnap in snapshot.children){
//                    val empData = empSnap.getValue(EmployeeModel::class.java)
//                    // do something with the matching employees
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // handle error
//            }
//        })


        dbRef = FirebaseDatabase.getInstance().getReference("Volunteering")
        val query = dbRef.orderByChild("gardenId").equalTo("-NUkAv0dxuexFf-vL5I8")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                volList.clear()
                if (snapshot.exists()){
                    for (volSnap in snapshot.children){
                        val volData = volSnap.getValue(VolunteeringModel::class.java)
                        volList.add(volData!!)
                    }
                    val mAdapter = VolAdapter(volList)
                    volRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : VolAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivityOwner, VolunteeringDetailsActivityOwner::class.java)

                            //put extras
                            intent.putExtra("volId", volList[position].volunteeringId)
                            intent.putExtra("hours", volList[position].hours)
                            intent.putExtra("date", volList[position].date)
                            intent.putExtra("garden", volList[position].gardenName)
                            intent.putExtra("status", volList[position].status)
                            startActivity(intent)
                        }

                    })

                    volRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}