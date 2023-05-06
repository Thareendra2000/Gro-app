package com.example.groapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.groapp.Activities.Cart.CartPendingActivity
import com.example.groapp.Adapters.CartPendingAdapter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class CartPendingActivityTest {

    @Before
    fun setUp() {
        ActivityScenario.launch(CartPendingActivity::class.java)
    }

    @Test
    fun verifyCartNavigationButtons() {
        // Verify that the back button is displayed and clickable
        onView(withId(R.id.backImg)).check(matches(isDisplayed())).perform(click())

        // Verify that the "Pending" button is displayed and clickable
        onView(withId(R.id.tvPending)).check(matches(isDisplayed())).perform(click())

        // Verify that the "Pick Up" button is displayed and clickable
        onView(withId(R.id.tvPickUp)).check(matches(isDisplayed())).perform(click())

        // Verify that the "Completed" button is displayed and clickable
        onView(withId(R.id.tvCompleted)).check(matches(isDisplayed())).perform(click())
    }


}