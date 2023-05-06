package com.example.groapp
//import androidx.test.core.app.ActivityScenario
//import androidx.test.espresso.Espresso.*
//import androidx.test.espresso.action.ViewActions.*
//import androidx.test.espresso.assertion.ViewAssertions.*
//import androidx.test.espresso.matcher.ViewMatchers.*
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.groapp.Activities.Garden.AddGardenActivity
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class AddGardenActivityTest {
//
//    @Test
//    fun testSaveGardenData() {
//        // Launch the activity under test
//        ActivityScenario.launch(AddGardenActivity::class.java)
//
//        // Find the input fields and submit button
//        val nameField = onView(withId(R.id.gardenName))
//        val addressField = onView(withId(R.id.gardenAddress))
//        val phoneNoField = onView(withId(R.id.gardenPhoneNo))
//        val locationField = onView(withId(R.id.gardenLocation))
//        val areaField = onView(withId(R.id.gardenArea))
//        val descriptionField = onView(withId(R.id.gardenDescription))
//        val submitButton = onView(withId(R.id.gardenSubmitBtn))
//
//        // Enter valid data into the input fields
//        nameField.perform(typeText("My Garden"), closeSoftKeyboard())
//        addressField.perform(typeText("123 Main St"), closeSoftKeyboard())
//        phoneNoField.perform(typeText("555-555-5555"), closeSoftKeyboard())
//        locationField.perform(typeText("https://example.com"), closeSoftKeyboard())
//        areaField.perform(typeText("1000 sq. ft."), closeSoftKeyboard())
//        descriptionField.perform(typeText("A beautiful garden"), closeSoftKeyboard())
//
//        // Click the submit button
//        submitButton.perform(click())
//
//        // Verify that a success toast is displayed
//        onView(withText("Data inserted successfully"))
//            .inRoot(withDecorView(not(`is`(activity.window.decorView))))
//            .check(matches(isDisplayed()))
//    }
//}
