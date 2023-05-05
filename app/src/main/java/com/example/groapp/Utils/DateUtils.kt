package com.example.groapp.Utils

import java.text.SimpleDateFormat
import java.util.*
import android.text.format.DateUtils

class DateUtils {
    companion object {
        private const val MINUTE_IN_MILLIS = 60 * 1000L
        private const val HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS
        private const val DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS
        private const val DATE_FORMAT = "yyyy-MM-dd"

        fun formatDate(date: Date): String {
            val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            return formatter.format(date)
        }

        fun getTimeAgoString(date: Date): String {
            val now = System.currentTimeMillis()
            val time = date.time
            val diff = now - time
            return when {
                diff < DateUtils.MINUTE_IN_MILLIS -> "just now"
                diff < 2 * DateUtils.MINUTE_IN_MILLIS -> "a minute ago"
                diff < DateUtils.HOUR_IN_MILLIS -> "${diff / DateUtils.MINUTE_IN_MILLIS} minutes ago"
                diff < 2 * DateUtils.HOUR_IN_MILLIS -> "an hour ago"
                diff < DateUtils.DAY_IN_MILLIS -> "${diff / DateUtils.HOUR_IN_MILLIS} hours ago"
                diff < 2 * DateUtils.DAY_IN_MILLIS -> "yesterday"
                else -> DateUtils.getRelativeTimeSpanString(date.time).toString()
            }
        }
    }
}
