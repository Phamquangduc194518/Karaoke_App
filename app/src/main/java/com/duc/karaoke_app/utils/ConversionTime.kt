package com.duc.karaoke_app.utils

import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ConversionTime {
    fun formatRelativeTimePretty(createdAt: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val time: Date = sdf.parse(createdAt) ?: Date()
        val prettyTime = PrettyTime(Locale.getDefault())
        return prettyTime.format(time)
    }
}