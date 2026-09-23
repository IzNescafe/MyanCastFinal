package com.example.myancast.ui.search

import com.example.myancast.FakePodcastRepository
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val podcasts = listOf(
        Podcast(id = "p1", title = "နည်းပညာ စကားဝိုင်း", category = "နည်းပညာ"),
        Podcast(id = "p2", title = "မနက်ခင်း သတင်းလွှာ", category = "သတင်း")
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
    fun `starts idle with no results`() = runTest {
        val vm = SearchViewModel(FakePodcastRepository(podcasts))

        val collectJob = launch { vm.state.collect { } }
        advanceUntilIdle()

        assertTrue(vm.state.value.isIdle)
        assertTrue(vm.state.value.results.isEmpty())
        assertFalse(vm.state.value.isLoading)

        collectJob.cancel()
    }

    @Test
    fun `query filters the podcasts`() = runTest {
        val vm = SearchViewModel(FakePodcastRepository(podcasts))

        val collectJob = launch { vm.state.collect { } }
        vm.onQueryChange("နည်း")
        advanceUntilIdle()

        assertFalse(vm.state.value.isIdle)
        assertEquals(listOf("p1"), vm.state.value.results.map { it.id })

        collectJob.cancel()
    }

    @Test
    fun `query with no match keeps the list empty but not idle`() = runTest {
        val vm = SearchViewModel(FakePodcastRepository(podcasts))

        val collectJob = launch { vm.state.collect { } }
        vm.onQueryChange("ဘောလုံး")
        advanceUntilIdle()

        assertFalse(vm.state.value.isIdle)   // ← "ရှာမတွေ့ပါ" ပြရမယ် ("ရိုက်ပါ" မဟုတ်)
        assertTrue(vm.state.value.results.isEmpty())

        collectJob.cancel()
    }

    @Test
    fun `clearQuery goes back to idle`() = runTest {
        val vm = SearchViewModel(FakePodcastRepository(podcasts))

        val collectJob = launch { vm.state.collect { } }
        vm.onQueryChange("နည်း")
        advanceUntilIdle()
        vm.clearQuery()
        advanceUntilIdle()

        assertTrue(vm.state.value.isIdle)
        assertEquals("", vm.state.value.query)
        assertTrue(vm.state.value.results.isEmpty())

        collectJob.cancel()
    }
}
