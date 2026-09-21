package com.example.myancast.domain.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DateFormatterTest {

    @Test
    fun `null timestamp is blank`() {
        assertEquals("", formatRelativeDate(null))
    }
}
