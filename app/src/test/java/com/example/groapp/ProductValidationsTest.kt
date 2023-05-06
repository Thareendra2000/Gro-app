package com.example.groapp
import android.util.Log
import com.example.groapp.Utils.ProductValidations
import org.junit.Test
import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

class ProductValidationsTest {

    private val validations = ProductValidations()

    @Test
    fun containsSpecialChar_returnsTrue_whenInputContainsSpecialChar() {
        val input = "Hello World!"
        val result = validations.containsSpecialChar(input)
        assertTrue(result)
    }

    @Test
    fun containsSpecialChar_returnsFalse_whenInputDoesNotContainSpecialChar() {
        val input = "Hello World"
        val result = validations.containsSpecialChar(input)
        assertFalse(result)
    }

    @Test
    fun requiredCheck_returnsTrue_whenInputIsNotNullOrBlank() {
        val input = "Hello World"
        val result = validations.requiredCheck(input)
        assertTrue(result)
    }

    @Test
    fun requiredCheck_returnsFalse_whenInputIsNullOrBlank() {
        val input :String?  = null
        val result = validations.requiredCheck(input)
        assertFalse(result)
    }

    @Test
    fun doubleCheck_returnsTrue_whenInputIsDouble() {
        val input = "1.23"
        val result = validations.doubleCheck(input)
        assertTrue(result)
    }

    @Test
    fun doubleCheck_returnsFalse_whenInputIsNotDouble() {
        val input = "Hello World"
        val result = validations.doubleCheck(input)
        assertFalse(result)
    }

    @Test
    fun validateBestBefore_returnsErrorMessage_whenDateIsBeforeToday() {
        val input = "01/01/2000"
        val result = validations.validateBestBefore(input)
        assertNotNull(result)
        assertEquals(result, "Best before date must be a day after today")
    }

    @Test
    fun validateBestBefore_returnsErrorMessage_whenDateIsToday() {
        val input = SimpleDateFormat("dd/MM/yyyy").format(Date())
        val result = validations.validateBestBefore(input)
        assertNotNull(result)
        assertEquals(result, "Best before date cannot be today")
    }

    @Test
    fun validateBestBefore_returnsNull_whenDateIsAfterToday() {
        val input = SimpleDateFormat("dd/MM/yyyy").format(Date(Date().time + 86400000))
        val result = validations.validateBestBefore(input)
        assertNull(result)
    }
}
