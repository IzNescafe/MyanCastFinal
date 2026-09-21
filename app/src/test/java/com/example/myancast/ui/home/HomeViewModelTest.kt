package com.example.myancast.ui.home

import com.example.myancast.data.repository.PodcastRepository
import com.example.myancast.domain.model.Podcast
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val repo: PodcastRepository = mockk()

    private val fakePodcasts = listOf(
        Podcast(id = "p1", title = "One", category = "သတင်း"),
        Podcast(id = "p2", title = "Two", category = "နည်းပညာ"),
        Podcast(id = "p3", title = "Three", category = "ဇာတ်လမ်း"),
        Podcast(id = "p4", title = "Four", category = "ကျန်းမာရေး"),
        Podcast(id = "p5", title = "Five", category = "စီးပွားရေး")
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
    fun `state is loading before data arrives`() {
        every { repo.getPodcasts() } returns emptyFlow()

        val vm = HomeViewModel(repo)

        assertTrue(vm.state.value.isLoading)
        assertTrue(vm.state.value.podcasts.isEmpty())
    }

    @Test
    fun `loads five podcasts and builds categories`() {
        every { repo.getPodcasts() } returns flowOf(fakePodcasts)

        val vm = HomeViewModel(repo)
        val state = vm.state.value

        assertEquals(false, state.isLoading)
        assertEquals(5, state.podcasts.size)
        assertEquals(5, state.allPodcasts.size)
        assertEquals(6, state.categories.size) // "အားလုံး" + 5
        assertEquals(ALL_CATEGORY, state.categories.first())
    }

    @Test
    fun `selectCategory filters podcasts`() {
        every { repo.getPodcasts() } returns flowOf(fakePodcasts)

        val vm = HomeViewModel(repo)
        vm.selectCategory("သတင်း")

        assertEquals(listOf("p1"), vm.state.value.podcasts.map { it.id })
        assertEquals(5, vm.state.value.allPodcasts.size)
    }
}
