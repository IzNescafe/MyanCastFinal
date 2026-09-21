// app/src/test/java/com/example/myancast/ui/home/HomeViewModelTest.kt
package com.example.myancast.ui.home

import com.example.myancast.FakePodcastRepository
import com.example.myancast.domain.model.Podcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class HomeViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val fakeData = listOf(
        Podcast("1", "နည်းပညာ", "desc", "", "နည်းပညာ", 5),
        Podcast("2", "သတင်း", "desc", "", "သတင်း", 3),
        Podcast("3", "ဇာတ်လမ်း", "desc", "", "ဇာတ်လမ်း", 2),
        Podcast("4", "ကျန်းမာရေး", "desc", "", "ကျန်းမာရေး", 1),
        Podcast("5", "စီးပွားရေး", "desc", "", "စီးပွားရေး", 4)
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
    fun `initial state is loading`() = runTest {
        val vm = HomeViewModel(FakePodcastRepository(fakeData))
        assertTrue(vm.state.value.isLoading)
    }

    @Test
    fun `after load, podcasts has 5 items`() = runTest {
        val vm = HomeViewModel(FakePodcastRepository(fakeData))
        advanceUntilIdle()

        assertEquals(5, vm.state.value.podcasts.size)
        assertFalse(vm.state.value.isLoading)
        assertEquals(null, vm.state.value.error)
    }
}