package com.example.myancast.domain.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DurationFormatterTest {

    @Test
    fun `formats minutes and seconds`() {
        assertEquals("24:30", formatDuration(1470))
        assertEquals("1:02:05", formatDuration(3725))
    }

    @Test
    fun `zero or negative is 0_00`() {
        assertEquals("0:00", formatDuration(0))
        assertEquals("0:00", formatDuration(-5))
    }

    @Test
    fun `duration label uses Myanmar digits and hours`() {
        // ပုံမှန် မိနစ်
        assertEquals("၂၄ မိနစ်", formatDurationLabel(1470))

        // နာရီ + မိနစ်
        assertEquals("၁ နာရီ ၂ မိနစ်", formatDurationLabel(3725))

        // နာရီ ပြည့်
        assertEquals("၁ နာရီ", formatDurationLabel(3600))
        assertEquals("၂ နာရီ", formatDurationLabel(7200))

        // ၁ မိနစ် အောက်
        assertEquals("၁ မိနစ် အောက်", formatDurationLabel(45))
        assertEquals("၁ မိနစ် အောက်", formatDurationLabel(59))

        // သုည နဲ့ အနုတ် — crash မဖြစ်ရ
        assertEquals("", formatDurationLabel(0))
        assertEquals("", formatDurationLabel(-5))
    }
}
