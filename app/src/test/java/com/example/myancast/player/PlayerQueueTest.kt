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

    private val queue = PlayerQueue(emptyList(), 0)

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
    fun `from with unknown id falls back to index 0`() {
        // Caller (PodcastDetailViewModel) က ID ကို ပြန်စစ်ရမယ်
        assertEquals(0, PlayerQueue.from(episodes, "missing").startIndex)
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
        assertEquals(45_000L, queue.skipBackTarget(60_000L))
        assertEquals(0L, queue.skipBackTarget(10_000L))
    }

    @Test
    fun `skipForward goes 30s but not past duration`() {
        assertEquals(90_000L, queue.skipForwardTarget(60_000L, 600_000L))
        assertEquals(100_000L, queue.skipForwardTarget(90_000L, 100_000L))
    }

    @Test
    fun `skipForward with unknown duration just adds 30s`() {
        assertEquals(40_000L, queue.skipForwardTarget(10_000L, 0L))
    }

    // ──────── Speed ────────

    @Test
    fun `nextSpeed cycles through all speeds and wraps`() {
        var speed = 0.5f
        val seen = mutableListOf(speed)
        repeat(6) {
            speed = queue.nextSpeed(speed)
            seen += speed
        }
        assertEquals(listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f, 0.5f), seen)
    }

    @Test
    fun `nextSpeed with unknown speed restarts from the list`() {
        assertEquals(0.75f, queue.nextSpeed(1.1f))
    }
}
