package com.example.myancast.ui.details

import com.example.myancast.FakeLibraryRepository
import com.example.myancast.FakePlayerController
import com.example.myancast.data.repository.PodcastRepository
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class PodcastDetailViewModelTest {

    private val repo: PodcastRepository = mockk()
    private val player = FakePlayerController()
    private val libraryRepo = FakeLibraryRepository()          // ★ အသစ်

    private val podcast = Podcast(id = "p1", title = "Podcast", coverUrl = "cover.jpg")

    private val episodes = listOf(
        Episode(id = "e1", podcastId = "p1", title = "One"),
        Episode(id = "e2", podcastId = "p1", title = "Two", coverUrl = "own.jpg")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads podcast and episodes`() {
        coEvery { repo.getPodcast("p1") } returns podcast
        every { repo.getEpisodes("p1") } returns flowOf(episodes)

        val vm = PodcastDetailViewModel("p1", repo, player, libraryRepo)   // ★
        val state = vm.state.value

        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(podcast, state.podcast)
        assertEquals(2, state.episodes.size)
        assertEquals("cover.jpg", state.episodes[0].coverUrl)
        assertEquals("own.jpg", state.episodes[1].coverUrl)
    }

    @Test
    fun `missing podcast shows not-found error`() {
        coEvery { repo.getPodcast("missing") } returns null

        val vm = PodcastDetailViewModel("missing", repo, player, libraryRepo)   // ★
        val state = vm.state.value

        assertFalse(state.isLoading)
        assertNull(state.podcast)
        assertEquals("Podcast ရှာမတွေ့ပါ", state.error)
        assertFalse(state.canRetry)
        verify(exactly = 0) { repo.getEpisodes(any()) }
    }

    // ──────── Detail → play ────────

    private val playable = listOf(
        Episode(id = "e1", podcastId = "p1", title = "One", audioUrl = "https://a/1.mp3"),
        Episode(id = "e2", podcastId = "p1", title = "No audio"),
        Episode(id = "e3", podcastId = "p1", title = "Three", audioUrl = "https://a/3.mp3")
    )

    private fun loadedVm(): PodcastDetailViewModel {
        coEvery { repo.getPodcast("p1") } returns podcast
        every { repo.getEpisodes("p1") } returns flowOf(playable)
        return PodcastDetailViewModel("p1", repo, player, libraryRepo)   // ★
    }

    @Test
    fun `playEpisode queues playable episodes and starts at the tapped one`() {
        val vm = loadedVm()

        assertTrue(vm.playEpisode("e3"))

        val queue = player.lastQueue!!
        assertEquals(listOf("e1", "e3"), queue.episodes.map { it.id })
        assertEquals("e3", queue.episodes[queue.startIndex].id)
        assertEquals("Podcast", player.lastPodcastTitle)
        assertEquals("e3", player.state.value.currentEpisode?.id)
    }

    @Test
    fun `playEpisode without audio does not play and shows error`() {
        val vm = loadedVm()

        assertFalse(vm.playEpisode("e2"))

        assertEquals(0, player.playCallCount)
        assertEquals("ဒီအပိုင်းကို ဖွင့်လို့ မရပါ", vm.state.value.playError)

        vm.playErrorShown()
        assertNull(vm.state.value.playError)
    }

    @Test
    fun `playAll starts at the first playable episode`() {
        val vm = loadedVm()

        assertTrue(vm.playAll())

        val queue = player.lastQueue!!
        assertEquals(0, queue.startIndex)
        assertEquals("e1", queue.episodes.first().id)
    }

    @Test
    fun `playAll with no playable episodes does not play`() {
        coEvery { repo.getPodcast("p1") } returns podcast
        every { repo.getEpisodes("p1") } returns flowOf(episodes)
        val vm = PodcastDetailViewModel("p1", repo, player, libraryRepo)   // ★

        assertFalse(vm.playAll())

        assertEquals(0, player.playCallCount)
        assertEquals("ဖွင့်လို့ရတဲ့ အပိုင်း မရှိပါ", vm.state.value.playError)
    }
}