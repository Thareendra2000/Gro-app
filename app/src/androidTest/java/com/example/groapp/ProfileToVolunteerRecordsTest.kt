package com.example.tute5

import android.content.Intent
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.groapp.Activities.MyProfileActivity
import com.example.groapp.Activities.ProfileMain
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import com.example.groapp.R

@RunWith(AndroidJUnit4::class)
class  ProfileToVolunteerRecordsTest{

    @Test
    fun profileToVolunteerRecordsTest() {
        // Start the activity
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileMain::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val activityScenario = ActivityScenario.launch<ProfileMain>(intent)


        // Find and click the marketPlace button
        onView(withId(R.id.volEfforts)).perform(click())

        // Check that the text of tvHeader in the next screen is "Buyer MarketPlace"
        onView(withId(R.id.volRecordsTopic)).check { view, _ ->
            assertEquals("Volunteering Records", view?.findViewById<TextView>(R.id.volRecordsTopic)?.text)
        }

        // Close the activity
        activityScenario.close()
    }

    @Test
    fun profile0ToVolunteerRecordsTest() {
        // Start the activity
        val intent = Intent(ApplicationProvider.getApplicationContext(), MyProfileActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val activityScenario = ActivityScenario.launch<ProfileMain>(intent)


        // Find and click the marketPlace button
        onView(withId(R.id.volEfforts)).perform(click())

        // Check that the text of tvHeader in the next screen is "Buyer MarketPlace"
        onView(withId(R.id.volRecordsTopic)).check { view, _ ->
            assertEquals("Volunteering Records", view?.findViewById<TextView>(R.id.volRecordsTopic)?.text)
        }

        // Close the activity
        activityScenario.close()
    }

}
