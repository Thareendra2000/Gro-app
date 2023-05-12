import com.example.groapp.Utils.VolunteerValidations
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class VolunteerValidationsTest {

    private val validator = VolunteerValidations()

    @Test
    fun testIsValidHours() {
        Assert.assertTrue(validator.isValidHours("3"))
        Assert.assertTrue(validator.isValidHours("7"))
        Assert.assertFalse(validator.isValidHours("0"))
        Assert.assertFalse(validator.isValidHours("8"))
        Assert.assertFalse(validator.isValidHours("-5"))
    }
//
//    @Test
//    fun testIsValidYear() {
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
//        val today = dateFormat.parse(dateFormat.format(Date()))
//
//        // Test with valid dates within 30 days from today
//        val validDates = listOf(
//            today,
//            Date(today.time + 1000L * 60L * 60L * 24L * 15L),  // 15 days from today
//            Date(today.time - 1000L * 60L * 60L * 24L * 30L)   // 30 days ago
//        )
//        for (date in validDates) {
//            val formattedDate = dateFormat.format(date)
//            Assert.assertTrue(validator.isValidYear(formattedDate))
//        }
//
//        // Test with invalid dates more than 30 days from today
//        val invalidDates = listOf(
//            Date(today.time + 1000L * 60L * 60L * 24L * 31L),  // 31 days from today
//            Date(today.time + 1000L * 60L * 60L * 24L * 60L),  // 60 days from today
//            Date(today.time - 1000L * 60L * 60L * 24L * 100L)  // 100 days ago
//        )
//        for (date in invalidDates) {
//            val formattedDate = dateFormat.format(date)
//            Assert.assertFalse(validator.isValidYear(formattedDate))
//        }
//
//        // Test with invalid date format
//        Assert.assertFalse(validator.isValidYear("2023-05-12"))
//    }
}
