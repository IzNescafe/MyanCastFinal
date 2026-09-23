// app/src/test/java/com/example/myancast/ui/home/HomeViewModelTest.kt
package com.example.myancast.ui.home

import com.example.myancast.FakeLibraryRepository
import com.example.myancast.FakePlayerController
import com.example.myancast.FakePodcastRepository
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.PlaybackProgress
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

    /** Phase 4 — HomeViewModel က history နဲ့ player ကိုပါ လိုတယ် (ဆက်နားထောင်ရန် အတွက်) */
    private fun viewModel(
        podcasts: List<Podcast> = fakeData,
        episodes: List<Episode> = emptyList(),
        history: List<PlaybackProgress> = emptyList(),
        player: FakePlayerController = FakePlayerController()
    ) = HomeViewModel(
        repo = FakePodcastRepository(podcasts, episodes),
        libraryRepo = FakeLibraryRepository(initialHistory = history),
        player = player
    )

    @Test
    fun `initial state is loading`() = runTest {
        val vm = viewModel()
        assertTrue(vm.state.value.isLoading)
    }

    @Test
    fun `after load, podcasts has 5 items`() = runTest {
        val vm = viewModel()
        advanceUntilIdle()

        assertEquals(5, vm.state.value.podcasts.size)
        assertFalse(vm.state.value.isLoading)
        assertEquals(null, vm.state.value.error)
    }

    // ─── Phase 4: ဆက်နားထောင်ရန် ───

    private val episode = Episode(
        id = "e1",
        podcastId = "1",
        title = "အပိုင်း ၁",
        audioUrl = "https://a/1.mp3"
    )

    private fun progress(positionMs: Long = 30_000L, durationMs: Long = 100_000L) =
        PlaybackProgress(
            episodeId = "e1",
            podcastId = "1",
            positionMs = positionMs,
            durationMs = durationMs,
            updatedAt = 1L,
            episodeTitle = "အပိုင်း ၁",
            podcastTitle = "နည်းပညာ",
            coverUrl = "cover.jpg"
        )

    @Test
    fun `lastPlayed comes from history`() = runTest {
        val vm = viewModel(history = listOf(progress()))
        advanceUntilIdle()

        assertEquals("e1", vm.state.value.lastPlayed?.episodeId)
        assertEquals(0.3f, vm.state.value.lastPlayed?.fraction)
    }

    @Test
    fun `finished episode is not offered to continue`() = runTest {
        // ၉၅% ကျော် — ပြီးသွားပြီမို့ card မပြရ
        val vm = viewModel(history = listOf(progress(positionMs = 99_000L)))
        advanceUntilIdle()

        assertEquals(null, vm.state.value.lastPlayed)
    }

    @Test
    fun `resumeLastPlayed plays the episode from the saved position`() = runTest {
        val player = FakePlayerController()
        val vm = viewModel(
            episodes = listOf(episode),
            history = listOf(progress()),
            player = player
        )
        advanceUntilIdle()

        assertTrue(vm.resumeLastPlayed())
        advanceUntilIdle()

        assertEquals(1, player.playCallCount)
        assertEquals("e1", player.lastQueue?.episodes?.first()?.id)
        assertEquals("နည်းပညာ", player.lastPodcastTitle)
        // ⚠️ seekTo() မဟုတ်ဘဲ play() ကိုပဲ position ပေးရမယ် —
        // app အသစ်ဖွင့်ချိန် service မချိတ်ရသေးရင် seek ပျောက်တယ်
        assertEquals(30_000L, player.lastStartPositionMs)
    }

    @Test
    fun `resumeLastPlayed does not restart what is already playing`() = runTest {
        val player = FakePlayerController()
        val vm = viewModel(
            episodes = listOf(episode),
            history = listOf(progress()),
            player = player
        )
        advanceUntilIdle()

        vm.resumeLastPlayed()          // ပထမ တစ်ခါ — စဖွင့်
        advanceUntilIdle()
        assertEquals(1, player.playCallCount)

        vm.resumeLastPlayed()          // ဖွင့်ထားပြီးသား — ပြန်မစရ
        advanceUntilIdle()
        assertEquals(1, player.playCallCount)
    }

    @Test
    fun `card shows pause only while that episode plays`() = runTest {
        val player = FakePlayerController()
        val vm = viewModel(
            episodes = listOf(episode),
            history = listOf(progress()),
            player = player
        )
        advanceUntilIdle()
        assertFalse(vm.state.value.isLastPlayedPlaying)

        vm.resumeLastPlayed()          // FakePlayerController က isPlaying = true ပေးတယ်
        advanceUntilIdle()
        assertTrue(vm.state.value.isLastPlayedPlaying)

        vm.toggleLastPlayed()          // ရပ်လိုက် — icon က ▶ ပြန်ဖြစ်ရမယ်
        advanceUntilIdle()
        assertFalse(vm.state.value.isLastPlayedPlaying)
    }

    @Test
    fun `toggleLastPlayed pauses instead of restarting`() = runTest {
        val player = FakePlayerController()
        val vm = viewModel(
            episodes = listOf(episode),
            history = listOf(progress()),
            player = player
        )
        advanceUntilIdle()

        vm.resumeLastPlayed()
        advanceUntilIdle()

        vm.toggleLastPlayed()
        advanceUntilIdle()

        assertEquals(1, player.toggleCallCount)   // toggle ခေါ်တယ်
        assertEquals(1, player.playCallCount)     // play ထပ်မခေါ်ဘူး
    }

    @Test
    fun `toggleLastPlayed starts from the saved position when nothing is loaded`() = runTest {
        val player = FakePlayerController()
        val vm = viewModel(
            episodes = listOf(episode),
            history = listOf(progress()),
            player = player
        )
        advanceUntilIdle()

        vm.toggleLastPlayed()
        advanceUntilIdle()

        assertEquals(1, player.playCallCount)
        assertEquals(30_000L, player.lastStartPositionMs)
        assertEquals(0, player.toggleCallCount)
    }

    @Test
    fun `unplayable episode shows a message instead of failing silently`() = runTest {
        val player = FakePlayerController()
        val vm = viewModel(
            episodes = listOf(episode.copy(audioUrl = "")),   // audio မရှိ
            history = listOf(progress()),
            player = player
        )
        advanceUntilIdle()

        vm.resumeLastPlayed()
        advanceUntilIdle()

        assertEquals(0, player.playCallCount)
        assertEquals("ဒီအပိုင်းကို ဖွင့်လို့ မရပါ", vm.state.value.resumeError)

        vm.resumeErrorShown()
        assertEquals(null, vm.state.value.resumeError)
    }

    @Test
    fun `resumeLastPlayed does nothing without history`() = runTest {
        val player = FakePlayerController()
        val vm = viewModel(player = player)
        advanceUntilIdle()

        assertFalse(vm.resumeLastPlayed())
        advanceUntilIdle()

        assertEquals(0, player.playCallCount)
    }
}