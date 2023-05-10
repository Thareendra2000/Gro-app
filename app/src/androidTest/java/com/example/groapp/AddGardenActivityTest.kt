package com.example.groapp
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.groapp.Activities.Garden.AddGardenActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddGardenActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(AddGardenActivity::class.java)

    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phoneNo: String
    private lateinit var location: String
    private lateinit var description: String
    private lateinit var area: String

    @Before
    fun setUp() {
        name = "Test Garden"
        address = "Test Address"
        phoneNo = "1234567890"
        location = "Test Location URL"
        description = "Test Description"
        area = "Test Area"
    }

    @Test
    fun testSaveGardenData() {
        onView(withId(R.id.gardenName)).perform(clearText(), typeText(name), closeSoftKeyboard())
        onView(withId(R.id.gardenAddress)).perform(clearText(), typeText(address), closeSoftKeyboard())
        onView(withId(R.id.gardenPhoneNo)).perform(clearText(), typeText(phoneNo), closeSoftKeyboard())
        onView(withId(R.id.gardenLocation)).perform(clearText(), typeText(location), closeSoftKeyboard())
        onView(withId(R.id.gardenArea)).perform(clearText(), typeText(area), closeSoftKeyboard())
        onView(withId(R.id.gardenDescription)).perform(clearText(), typeText(description), closeSoftKeyboard())
        onView(withId(R.id.gardenSubmitBtn)).perform(click())
        onView(withText("Data inserted successfully")).check(matches(isDisplayed()))
    }
}
