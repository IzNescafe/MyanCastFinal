package com.example.myancast.domain.util

/** စက္ကန့် → "24:30" (သို့) "1:02:05" */
fun formatDuration(seconds: Int): String {
    if (seconds <= 0) return "0:00"
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s)
    else "%d:%02d".format(m, s)
}

/** စက္ကန့် → "24 မိနစ်"၊ 0 (သို့) အနုတ်ဆိုရင် "" (UI မှာ မပြ) */
fun formatDurationLabel(seconds: Int): String {
    if (seconds <= 0) return ""
    val minutes = seconds / 60
    return "$minutes မိနစ်"
}
