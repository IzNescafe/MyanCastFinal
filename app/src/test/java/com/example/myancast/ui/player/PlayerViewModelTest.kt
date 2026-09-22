package com.example.myancast.ui.player

import com.example.myancast.FakePlayerController
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.PlaybackState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModelTest {

    private val episodes = listOf(
        Episode(id = "e1", title = "One", coverUrl = "c1.jpg", duration = 1470),
        Episode(id = "e2", title = "Two")
    )

    private val playing = PlaybackState(
        queue = episodes,
        currentIndex = 0,
        podcastTitle = "Podcast",
        isPlaying = true,
        positionMs = 725_000L,
        durationMs = 1_470_000L,
        speed = 1.5f
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ──────── Pure mapping ────────

    @Test
    fun `nothing playing maps to no episode`() {
        val ui = PlaybackState().toUiState()
        assertFalse(ui.hasEpisode)
        assertEquals("1x", ui.speedText)
    }

    @Test
    fun `playing state maps titles, times and speed`() {
        val ui = playing.toUiState()

        assertTrue(ui.hasEpisode)
        assertEquals("One", ui.title)
        assertEquals("Podcast", ui.podcastTitle)
        assertEquals("c1.jpg", ui.coverUrl)
        assertEquals("12:05", ui.positionText)
        assertEquals("24:30", ui.durationText)
        assertEquals("1.5x", ui.speedText)
        assertTrue(ui.hasNext)
        assertFalse(ui.hasPrevious)
        assertNull(ui.error)
    }

    @Test
    fun `unknown duration falls back to episode duration`() {
        val ui = playing.copy(durationMs = 0L, positionMs = 0L).toUiState()
        assertEquals(1_470_000L, ui.durationMs)
        assertEquals("24:30", ui.durationText)
    }

    @Test
    fun `formatSpeed trims trailing zeros`() {
        assertEquals("0.5x", formatSpeed(0.5f))
        assertEquals("0.75x", formatSpeed(0.75f))
        assertEquals("1x", formatSpeed(1f))
        assertEquals("1.25x", formatSpeed(1.25f))
        assertEquals("2x", formatSpeed(2f))
    }

    // ──────── Actions ────────

    @Test
    fun `actions call the controller`() {
        val fake = FakePlayerController(playing)
        val vm = PlayerViewModel(fake)

        vm.togglePlayPause()
        vm.skipBack()
        vm.skipForward()
        vm.next()
        vm.previous()

        assertEquals(1, fake.toggleCallCount)
        assertEquals(1, fake.skipBackCount)
        assertEquals(1, fake.skipForwardCount)
        assertEquals(1, fake.nextCount)
        assertEquals(1, fake.previousCount)
    }

    @Test
    fun `seekToFraction converts to milliseconds`() {
        val fake = FakePlayerController(playing)
        val vm = PlayerViewModel(fake)

        vm.seekToFraction(0.5f)

        assertEquals(735_000L, fake.seekToMs)
    }

    @Test
    fun `seekToFraction does nothing without a duration`() {
        val fake = FakePlayerController(PlaybackState())
        val vm = PlayerViewModel(fake)

        vm.seekToFraction(0.5f)

        assertNull(fake.seekToMs)
    }

    @Test
    fun `cycleSpeed goes to the next speed`() {
        val fake = FakePlayerController(playing)   // 1.5x
        val vm = PlayerViewModel(fake)

        vm.cycleSpeed()

        assertEquals(2f, fake.lastSpeed)
    }
}
