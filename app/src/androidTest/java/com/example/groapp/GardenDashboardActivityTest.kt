package com.example.groapp
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.groapp.Activities.Garden.GardenDashboardActivity
import com.google.firebase.database.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Matchers
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class GardenDashboardActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(GardenDashboardActivity::class.java)

    @Test
    fun testMostProfitableProduct() {
        onView(withId(R.id.pName)).check(matches(withText("product")))
    }
}

class GardenDashboardActivityGardenCountTest1 {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(GardenDashboardActivity::class.java)

    @Test
    fun testGardenCount() {
        val expectedCount = 35 // replace with the expected count
        onView(withId(R.id.Amount)).check(matches(isDisplayed()))
        onView(withId(R.id.Amount)).check(matches(withText(expectedCount.toString())))
    }
}



class GardenDashboardActivityProductCountTest2 {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(GardenDashboardActivity::class.java)

    @Test
    fun testProductCount() {
        val expectedCount = 35 // replace with the expected count
        onView(withId(R.id.pTotal)).check(matches(isDisplayed()))
        onView(withId(R.id.pTotal)).check(matches(withText(expectedCount.toString())))
    }
}
class GardenDashboardActivityGardenCountTest2 {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(GardenDashboardActivity::class.java)

    @Test
    fun testGardenCount() {
        val expectedCount = 5L // replace with the expected count
        val database = FirebaseDatabase.getInstance().reference

        val signal = CountDownLatch(1)
        var gardensCount: Long? = null

        // fetch the garden count from Firebase
        database.child("Garden").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gardensCount = dataSnapshot.childrenCount
                signal.countDown()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                signal.countDown()
            }
        })

        // wait for data to be loaded from Firebase
        signal.await(10, TimeUnit.SECONDS)

        // check the garden count
        onView(withId(R.id.Amount)).check(matches(isDisplayed()))
        onView(withId(R.id.Amount)).check(matches(withText(gardensCount.toString())))
    }
}

class GardenDashboardActivityProductCountTest3 {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(GardenDashboardActivity::class.java)

    @Test
    fun testProductCount() {
        val expectedCount = 10L // replace with the expected count
        val database = FirebaseDatabase.getInstance().reference

        val signal = CountDownLatch(1)
        var productsCount: Long? = null

        // fetch the product count from Firebase
        database.child("products").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                productsCount = dataSnapshot.childrenCount
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
        onView(withId(R.id.pTotal)).check(matches(isDisplayed()))
        onView(withId(R.id.pTotal)).check(matches(withText(productsCount.toString())))
    }
}





