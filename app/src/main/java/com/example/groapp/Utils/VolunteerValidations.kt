package com.example.groapp.Utils

import java.text.SimpleDateFormat
import java.util.*

class VolunteerValidations() {

    public fun isValidHours(hours: String): Boolean {
        val hoursInt: Int = hours.toInt()
        return hoursInt in 1..7
    }

    fun isValidYear(date: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        dateFormat.isLenient = false
        try {
            val inputDate = dateFormat.parse(date)
            val today = Date()
            val daysBetween = ((inputDate.time - today.time) / (1000 * 60 * 60 * 24)).toInt()
            return daysBetween in 0..30
        } catch (e: Exception) {
            return false
        }
    }

}