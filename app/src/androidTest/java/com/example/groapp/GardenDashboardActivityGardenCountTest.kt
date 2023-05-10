package com.example.groapp

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.groapp.Activities.Garden.GardenDashboardActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class GardenDashboardActivityGardenCountTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(GardenDashboardActivity::class.java)

    @Test
    fun testProductCount() {
        val expectedCount = 7L // replace with the expected count
        val database = FirebaseDatabase.getInstance().reference

        val signal = CountDownLatch(1)
        var gardenCount: Long? = null

        // fetch the product count from Firebase
        database.child("Garden").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gardenCount = dataSnapshot.childrenCount
                signal.countDown()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                signal.countDown()
            }
        })

        // wait for data to be loaded from Firebase
        signal.await(10, TimeUnit.SECONDS)

        // check the product count
        Espresso.onView(withId(R.id.Amount))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.Amount))
            .check(ViewAssertions.matches(ViewMatchers.withText(expectedCount.toString())))
    }
}