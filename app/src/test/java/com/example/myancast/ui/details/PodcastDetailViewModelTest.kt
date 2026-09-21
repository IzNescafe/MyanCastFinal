package com.example.myancast.ui.details

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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PodcastDetailViewModelTest {

    private val repo: PodcastRepository = mockk()

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

        val vm = PodcastDetailViewModel("p1", repo)
        val state = vm.state.value

        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(podcast, state.podcast)
        assertEquals(2, state.episodes.size)
        // Cover မရှိတဲ့ episode က podcast cover ကို ယူ
        assertEquals("cover.jpg", state.episodes[0].coverUrl)
        assertEquals("own.jpg", state.episodes[1].coverUrl)
    }

    @Test
    fun `missing podcast shows not-found error`() {
        coEvery { repo.getPodcast("missing") } returns null

        val vm = PodcastDetailViewModel("missing", repo)
        val state = vm.state.value

        assertFalse(state.isLoading)
        assertNull(state.podcast)
        assertEquals("Podcast ရှာမတွေ့ပါ", state.error)
        assertFalse(state.canRetry)
        verify(exactly = 0) { repo.getEpisodes(any()) }
    }
}
