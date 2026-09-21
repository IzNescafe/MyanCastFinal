package com.example.myancast.domain.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DateFormatterTest {

    // ပုံသေ "အခု" — test တိုင်း တူညီတဲ့ အဖြေ ရဖို့
    private val now = 1_750_000_000_000L

    private val MIN  = 60_000L
    private val HOUR = 60 * MIN
    private val DAY  = 24 * HOUR

    @Test
    fun `null timestamp returns empty`() {
        assertEquals("", formatRelativeDate(null))
    }

    @Test
    fun `under a minute is just now`() {
        assertEquals("ခုလေးတင်", formatRelativeMillis(now - 30_000L, now))
        assertEquals("ခုလေးတင်", formatRelativeMillis(now - 59_000L, now))
    }

    @Test
    fun `future date is just now`() {
        assertEquals("ခုလေးတင်", formatRelativeMillis(now + DAY, now))
        assertEquals("ခုလေးတင်", formatRelativeMillis(now + 5 * MIN, now))
    }

    @Test
    fun `minutes use Myanmar digits`() {
        assertEquals("၅ မိနစ် အရင်က", formatRelativeMillis(now - 5 * MIN, now))
        assertEquals("၃၀ မိနစ် အရင်က", formatRelativeMillis(now - 30 * MIN, now))
    }

    @Test
    fun `hours use Myanmar digits`() {
        assertEquals("၂ နာရီ အရင်က", formatRelativeMillis(now - 2 * HOUR, now))
        assertEquals("၂၃ နာရီ အရင်က", formatRelativeMillis(now - 23 * HOUR, now))
    }

    @Test
    fun `yesterday`() {
        assertEquals("မနေ့က", formatRelativeMillis(now - 30 * HOUR, now))
        assertEquals("မနေ့က", formatRelativeMillis(now - 47 * HOUR, now))
    }

    @Test
    fun `days use Myanmar digits`() {
        assertEquals("၅ ရက် အရင်က", formatRelativeMillis(now - 5 * DAY, now))
        assertEquals("၂၉ ရက် အရင်က", formatRelativeMillis(now - 29 * DAY, now))
    }

    @Test
    fun `old date uses Myanmar month name`() {
        val result = formatRelativeMillis(now - 45 * DAY, now)
        // မြန်မာ လ နာမည် တစ်ခုခု ပါရမယ် — အင်္ဂလိပ် မဖြစ်ရ
        assertTrue(
            "Expected Myanmar month in: $result",
            result.contains(Regex("ဇန်နဝါရီ|ဖေဖော်ဝါရီ|မတ်|ဧပြီ|မေ|ဇွန်|ဇူလိုင်|သြဂုတ်|စက်တင်ဘာ|အောက်တိုဘာ|နိုဝင်ဘာ|ဒီဇင်ဘာ"))
        )
        // မြန်မာ ဂဏန်း ပါရမယ်
        assertTrue(
            "Expected Myanmar digits in: $result",
            result.any { it in '၀'..'၉' }
        )
    }
}