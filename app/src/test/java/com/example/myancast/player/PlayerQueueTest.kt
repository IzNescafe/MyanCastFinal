package com.example.myancast.player

import com.example.myancast.domain.model.Episode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerQueueTest {

    private val episodes = listOf(
        Episode(id = "e1", audioUrl = "https://a/1.mp3"),
        Episode(id = "e2", audioUrl = ""),               // audio မရှိ
        Episode(id = "e3", audioUrl = "https://a/3.mp3"),
        Episode(id = "e4", audioUrl = "   ")             // blank
    )

    // ──────── from() ────────

    @Test
    fun `from drops episodes without audio`() {
        val q = PlayerQueue.from(episodes, null)
        assertEquals(listOf("e1", "e3"), q.episodes.map { it.id })
    }

    @Test
    fun `from starts at the requested episode`() {
        val q = PlayerQueue.from(episodes, "e3")
        assertEquals(1, q.startIndex)
        assertEquals("e3", q.episodes[q.startIndex].id)
    }

    @Test
    fun `from with null id starts at first playable`() {
        assertEquals(0, PlayerQueue.from(episodes, null).startIndex)
    }

    @Test
    fun `from with unknown id is empty`() {
        // index 0 ကို မပြန်ရ — နှိပ်တဲ့ဟာ မဟုတ်တာ ဖွင့်မိမယ်
        assertTrue(PlayerQueue.from(episodes, "missing").isEmpty)
    }

    @Test
    fun `from with a tapped episode that has no audio is empty`() {
        // e2 မှာ audioUrl မရှိ — တခြား episode ကို မဖွင့်မိစေရ
        assertTrue(PlayerQueue.from(episodes, "e2").isEmpty)
    }

    @Test
    fun `EMPTY has index minus one`() {
        assertEquals(-1, PlayerQueue.EMPTY.startIndex)
    }

    @Test
    fun `from with no playable episodes is empty`() {
        val q = PlayerQueue.from(listOf(Episode(id = "x")), "x")
        assertTrue(q.isEmpty)
        assertEquals(0, q.size)
    }

    // ──────── Skip helpers ────────

    @Test
    fun `skipBack goes back 15s but not below zero`() {
        assertEquals(45_000L, skipBackTarget(60_000L))
        assertEquals(0L, skipBackTarget(10_000L))
    }

    @Test
    fun `skipForward goes 30s but not past duration`() {
        assertEquals(90_000L, skipForwardTarget(60_000L, 600_000L))
        assertEquals(100_000L, skipForwardTarget(90_000L, 100_000L))
    }

    @Test
    fun `skipForward with unknown duration just adds 30s`() {
        assertEquals(40_000L, skipForwardTarget(10_000L, 0L))
    }

    // ──────── Speed ────────

    @Test
    fun `nextSpeed cycles through all speeds and wraps`() {
        var speed = 0.5f
        val seen = mutableListOf(speed)
        repeat(6) {
            speed = nextSpeed(speed)
            seen += speed
        }
        assertEquals(listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f, 0.5f), seen)
    }

    @Test
    fun `nextSpeed with unknown speed resets to normal`() {
        assertEquals(1f, nextSpeed(1.1f))
    }
}
