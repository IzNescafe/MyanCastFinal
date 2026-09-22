package com.example.myancast.ui.player

import com.example.myancast.FakePlayerController
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.PlaybackState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
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
class MiniPlayerViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val fakeEpisode = Episode(
        id = "1",
        title = "နည်းပညာ အခြေခံ",
        coverUrl = "https://picsum.photos/400",
        duration = 1500
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
    fun `hidden when no episode`() = runTest {
        val controller = FakePlayerController()
        val vm = MiniPlayerViewModel(controller)

        val collectJob = launch { vm.state.collect() }
        advanceUntilIdle()

        assertFalse(vm.state.value.visible)

        collectJob.cancel()
    }

    @Test
    fun `visible and mapped when episode plays`() = runTest {
        val controller = FakePlayerController()
        val vm = MiniPlayerViewModel(controller)

        // ★ Subscriber ထည့်
        val collectJob = launch { vm.state.collect() }

        controller.setState(
            PlaybackState(
                queue = listOf(fakeEpisode),
                currentIndex = 0,
                podcastTitle = "နည်းပညာနှင့် လူငယ်",
                isPlaying = true,
                durationMs = 1500_000L
            )
        )
        advanceUntilIdle()

        assertTrue(vm.state.value.visible)
        assertEquals("နည်းပညာ အခြေခံ", vm.state.value.title)
        assertEquals("နည်းပညာနှင့် လူငယ်", vm.state.value.subtitle)
        assertTrue(vm.state.value.isPlaying)

        collectJob.cancel()
    }

    @Test
    fun `togglePlayPause calls controller`() = runTest {
        val controller = FakePlayerController()
        val vm = MiniPlayerViewModel(controller)

        val collectJob = launch { vm.state.collect() }
        advanceUntilIdle()

        vm.togglePlayPause()

        assertEquals(1, controller.toggleCallCount)

        collectJob.cancel()
    }
}