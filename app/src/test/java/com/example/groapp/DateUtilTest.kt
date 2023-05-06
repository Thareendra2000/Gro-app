package com.example.groapp.Utils

import org.junit.Test
import org.junit.Assert.*
import java.util.*

class DateUtilTest {

    @Test
    fun testFormatDate() {
        val date = Date(1651148400000)
        val formattedDate = DateUtil.formatDate(date)
        assertEquals("2022-04-28", formattedDate)
    }

    @Test
    fun testGetTimeAgoString() {
        // Test current time
        val nowString = DateUtil.getTimeAgoString(Date())
        assertTrue(nowString.contains("just now"))

        // Test 1 minute ago
        val minuteAgo = Date(Date().time - DateUtil.MINUTE_IN_MILLIS)
        val minuteAgoString = DateUtil.getTimeAgoString(minuteAgo)
        assertTrue(minuteAgoString.contains("minute"))
        assertFalse(minuteAgoString.contains("minutes ago"))

        // Test 2 minutes ago
        val twoMinutesAgo = Date(Date().time - 2 * DateUtil.MINUTE_IN_MILLIS)
        val twoMinutesAgoString = DateUtil.getTimeAgoString(twoMinutesAgo)
        assertTrue(twoMinutesAgoString.contains("minute"))
        assertTrue(twoMinutesAgoString.contains("minutes ago"))

        // Test 1 hour ago
        val hourAgo = Date(Date().time - DateUtil.HOUR_IN_MILLIS)
        val hourAgoString = DateUtil.getTimeAgoString(hourAgo)
        assertTrue(hourAgoString.contains("hour"))
        assertFalse(hourAgoString.contains("hours ago"))

        // Test 2 hours ago
        val twoHoursAgo = Date(Date().time - 2 * DateUtil.HOUR_IN_MILLIS)
        val twoHoursAgoString = DateUtil.getTimeAgoString(twoHoursAgo)
        assertTrue(twoHoursAgoString.contains("hour"))
        assertTrue(twoHoursAgoString.contains("hours ago"))

        // Test yesterday
        val yesterday = Date(Date().time - DateUtil.DAY_IN_MILLIS)
        val yesterdayString = DateUtil.getTimeAgoString(yesterday)
        assertTrue(yesterdayString.contains("yesterday"))
    }
}
