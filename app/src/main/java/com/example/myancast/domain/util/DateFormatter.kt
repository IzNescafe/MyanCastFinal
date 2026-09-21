package com.example.myancast.domain.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

/** Timestamp → "ခုလေးတင်" / "5 မိနစ် အရင်က" / ... ၊ null ဆိုရင် "" */
fun formatRelativeDate(timestamp: Timestamp?): String {
    if (timestamp == null) return ""
    val diff = System.currentTimeMillis() - timestamp.toDate().time
    val sec = diff / 1000
    val min = sec / 60
    val hour = min / 60
    val day = hour / 24

    return when {
        sec < 60 -> "ခုလေးတင်"
        min < 60 -> "$min မိနစ် အရင်က"
        hour < 24 -> "$hour နာရီ အရင်က"
        day == 1L -> "မနေ့က"
        day < 30 -> "$day ရက် အရင်က"
        else -> SimpleDateFormat("d MMMM yyyy", Locale("my"))
            .format(timestamp.toDate())
    }
}
