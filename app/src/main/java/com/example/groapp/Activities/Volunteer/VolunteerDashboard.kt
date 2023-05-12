package com.example.groapp.Activities.Volunteer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.groapp.R
import com.example.groapp.Services.UserSingleton
import com.example.groapp.Services.AvgFun
import com.example.groapp.Services.volGardens
import com.example.groapp.Services.volHours

class VolunteerDashboard : AppCompatActivity() {

    private  var totalGardens: Int = 0
    private  var totalHours1: Int = 0
    private lateinit var statAvgHoursPerGarden: TextView
    private lateinit var statTotalHour: TextView
    private lateinit var statGardens: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_dashboard)


        statTotalHour = findViewById(R.id.statTotalHour)
        statAvgHoursPerGarden = findViewById(R.id.statAvgHoursPerGarden)
        statGardens = findViewById(R.id.statGardens)

        volGardens.calTotalUserVolGardens(UserSingleton.uid) { numUniqueGardens ->
            if (numUniqueGardens != null) {
                totalGardens = numUniqueGardens
            };
        }

        volHours.calTotalUserVolHours(UserSingleton.uid) { totalHours ->
            statTotalHour.text = totalHours.toString()
            if (totalHours != null) {
                totalHours1 =  totalHours
                statAvgHoursPerGarden.text =
                    AvgFun.avgHoursPerGarden(totalHours1,totalGardens).toString()
                statGardens.text = totalGardens.toString()
            }
        }


    }
}