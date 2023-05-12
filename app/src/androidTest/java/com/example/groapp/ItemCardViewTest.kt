import android.support.test.rule.ActivityTestRule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.groapp.Activities.MainActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import com.example.groapp.R;

@RunWith(AndroidJUnit4::class)
@LargeTest
class ItemCardLayoutTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testLayoutDisplayedCorrectly() {
        onView(withId(R.id.ItemCardDetails)).check(matches(isDisplayed()))
        onView(withId(R.id.tvCatItemImage)).check(matches(isDisplayed()))
        onView(withId(R.id.tvItemName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvItemDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.tvItemPrice)).check(matches(isDisplayed()))
        onView(withId(R.id.tvItemBestBefore)).check(matches(isDisplayed()))
        onView(withId(R.id.tvItemRatings)).check(matches(isDisplayed()))
    }
}
