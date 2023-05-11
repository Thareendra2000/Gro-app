package com.example.tute5
import com.example.groapp.Services.AvgFun
import org.junit.Test
import org.junit.Assert.*

class FunctionTest {

    @Test
    fun `should return the correct avg when provided with the amount and count `() {
        val avg = AvgFun.avgHoursPerGarden(10, 5)
        assertEquals(2, avg)
    }
}