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

/** စက္ကန့် → "၂၄ မိနစ်" / "၁ နာရီ ၂ မိနစ်" / "၁ မိနစ် အောက်"၊ 0 (သို့) အနုတ်ဆိုရင် "" (UI မှာ မပြ) */
fun formatDurationLabel(seconds: Int): String {
    if (seconds <= 0) return ""
    if (seconds < 60) return "၁ မိနစ် အောက်"
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    return when {
        h == 0 -> "${m.toMyanmarDigits()} မိနစ်"
        m == 0 -> "${h.toMyanmarDigits()} နာရီ"
        else -> "${h.toMyanmarDigits()} နာရီ ${m.toMyanmarDigits()} မိနစ်"
    }
}
