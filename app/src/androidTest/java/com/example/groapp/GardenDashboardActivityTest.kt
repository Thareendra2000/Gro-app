package com.example.groapp
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.groapp.Activities.Garden.GardenDashboardActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GardenDashboardActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(GardenDashboardActivity::class.java)

    @Test
    fun testMostProfitableProduct() {
        onView(withId(R.id.pName)).check(matches(withText("product")))
    }
}
