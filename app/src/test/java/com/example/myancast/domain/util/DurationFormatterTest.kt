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
    fun `label is minutes, blank when zero`() {
        assertEquals("24 မိနစ်", formatDurationLabel(1470))
        assertEquals("", formatDurationLabel(0))
    }
}
