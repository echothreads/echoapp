package com.echo.app

import android.icu.text.CompactDecimalFormat
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

object Utils {

    /**
     * Converts Instant to a string of the time passed
     */
    fun getTimeAgo(timestamp: Instant): String {
        val now = Clock.System.now()
        val duration: Duration = now - timestamp

        return when {
            duration.inWholeMinutes < 1 -> "Now"
            duration.inWholeMinutes < 60 -> "${duration.inWholeMinutes}m"
            duration.inWholeHours < 24 -> "${duration.inWholeHours}h"
            duration.inWholeDays < 7 -> "${duration.inWholeDays}d"
            else -> "${duration.inWholeDays/ 7}w"
        }
    }

    /**
     * Converts big numbers to short decimal strings
     */
    fun formatNumber(number: Number): String {
        val formatter = CompactDecimalFormat.getInstance(
            Locale.getDefault(),
            CompactDecimalFormat.CompactStyle.SHORT
        )
        return formatter.format(number)
    }
}