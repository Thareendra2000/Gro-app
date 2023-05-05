package com.example.groapp.Activities.Cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.Activities.HomeActivity
import com.example.groapp.Activities.MainActivity
import com.example.groapp.Adapters.EmpAdapter
import com.example.groapp.Models.EmployeeModel
import com.example.groapp.R
import com.google.firebase.database.*

class CartPendingActivity : AppCompatActivity() {
    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<EmployeeModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_pending)

        // cart navigation
        var backImg: ImageView = findViewById(R.id.backImg)
        backImg.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        var tvPending: Button = findViewById(R.id.tvPending)
        tvPending.setOnClickListener {
            val intent = Intent(this, CartPendingActivity::class.java)
            startActivity(intent)
        }
        var tvPickUp: Button = findViewById(R.id.tvPickUp)
        tvPickUp.setOnClickListener {
            val intent = Intent(this, CartPickUpsActivity::class.java)
            startActivity(intent)
        }
        var tvCompleted: Button = findViewById(R.id.tvCompleted)
        tvCompleted.setOnClickListener {
            val intent = Intent(this, CartCompletedActivity::class.java)
            startActivity(intent)
        }

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        empList = arrayListOf<EmployeeModel>()

        getEmployeesData()
    }

    private fun getEmployeesData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Employee")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val empData = empSnap.getValue(EmployeeModel::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(empList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

//                            val intent = Intent(this@FetchingActivity, EmployeeDetailsActivity::class.java)

                            //put extras
//                            intent.putExtra("empId", empList[position].empId)
//                            intent.putExtra("empName", empList[position].empName)
//                            intent.putExtra("empAge", empList[position].empAge)
//                            intent.putExtra("empSalary", empList[position].empSalary)
//                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}