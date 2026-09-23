package com.example.myancast.ui.library

import com.example.myancast.FakeLibraryRepository
import com.example.myancast.FakePodcastRepository
import com.example.myancast.domain.model.PlaybackProgress
import com.example.myancast.domain.model.Podcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val fakePodcasts = listOf(
        Podcast("p1", "Podcast 1", "", "", "Tech", 5),
        Podcast("p2", "Podcast 2", "", "", "News", 3)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `subscribed list filters by subscribedIds`() = runTest {
        val libraryRepo = FakeLibraryRepository(initialSubscribed = setOf("p1"))
        val podcastRepo = FakePodcastRepository(fakePodcasts)
        val vm = LibraryViewModel(libraryRepo, podcastRepo)

        val collectJob = launch { vm.state.collect { } }   // ← ★ ပြင်
        advanceUntilIdle()

        assertEquals(1, vm.state.value.subscribed.size)
        assertEquals("p1", vm.state.value.subscribed[0].id)
        assertFalse(vm.state.value.isLoading)

        collectJob.cancel()
    }

    @Test
    fun `history maps to HistoryRow`() = runTest {
        val progress = PlaybackProgress(
            episodeId = "ep1",
            podcastId = "p1",
            positionMs = 5000L,
            durationMs = 10000L,
            updatedAt = 1L,
            episodeTitle = "Episode 1",
            podcastTitle = "Podcast 1",
            coverUrl = "https://example.com/cover.jpg"
        )
        val libraryRepo = FakeLibraryRepository(initialHistory = listOf(progress))
        val podcastRepo = FakePodcastRepository(fakePodcasts)
        val vm = LibraryViewModel(libraryRepo, podcastRepo)

        val collectJob = launch { vm.state.collect { } }   // ← ★ ပြင်
        advanceUntilIdle()

        assertEquals(1, vm.state.value.history.size)
        assertEquals("Episode 1", vm.state.value.history[0].episodeTitle)
        assertEquals("Podcast 1", vm.state.value.history[0].podcastTitle)

        collectJob.cancel()
    }
}