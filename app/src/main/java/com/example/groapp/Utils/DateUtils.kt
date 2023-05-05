package com.example.groapp.Utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"

        fun formatDate(date: Date): String {
            val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            return formatter.format(date)
        }
    }
}
