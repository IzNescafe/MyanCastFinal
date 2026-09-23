// app/src/test/java/com/example/myancast/ui/news/NewsDetailViewModelTest.kt
package com.example.myancast.ui.news

import com.example.myancast.FakeNewsRepository
import com.example.myancast.FakePlayerController
import com.example.myancast.domain.model.NewsItem
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsDetailViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val withAudio = NewsItem(
        id = "1",
        headline = "အသံပါ သတင်း",
        body = "အကြောင်းအရာ",
        imageUrl = "https://example.com/1.jpg",
        audioUrl = "https://example.com/1.mp3",
        category = "နိုင်ငံရေး"
    )

    private val noAudio = NewsItem(
        id = "2",
        headline = "အသံမပါ သတင်း",
        body = "အကြောင်းအရာ",
        category = "အားကစား"
    )

    private val fakeNews = listOf(withAudio, noAudio)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(newsId: String, player: FakePlayerController = FakePlayerController()) =
        NewsDetailViewModel(newsId, FakeNewsRepository(fakeNews), player)

    // ─── Load ───────────────────────────────────

    @Test
    fun `loads the news item by id`() = runTest {
        val vm = viewModel("1")
        advanceUntilIdle()

        val state = vm.state.value
        assertEquals("အသံပါ သတင်း", state.news?.headline)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `unknown id shows not found and cannot retry`() = runTest {
        val vm = viewModel("999")
        advanceUntilIdle()

        val state = vm.state.value
        assertNull(state.news)
        assertNotNull(state.error)
        assertFalse(state.canRetry)
    }

    // ─── hasAudio ───────────────────────────────

    @Test
    fun `hasAudio is true only when audioUrl is present`() = runTest {
        val withAudioVm = viewModel("1")
        val noAudioVm = viewModel("2")
        advanceUntilIdle()

        assertTrue(withAudioVm.state.value.hasAudio)
        assertFalse(noAudioVm.state.value.hasAudio)
    }

    // ─── Play ───────────────────────────────────

    @Test
    fun `playAudio starts the player with the news as one episode`() = runTest {
        val player = FakePlayerController()
        val vm = viewModel("1", player)
        advanceUntilIdle()

        assertTrue(vm.playAudio())
        assertEquals(1, player.playCallCount)
        assertEquals(1, player.lastQueue?.size)
        assertEquals("အသံပါ သတင်း", player.lastQueue?.episodes?.first()?.title)
        assertEquals(NewsDetailViewModel.PODCAST_TITLE_NEWS, player.lastPodcastTitle)
    }

    @Test
    fun `playAudio without audio shows error and does not call player`() = runTest {
        val player = FakePlayerController()
        val vm = viewModel("2", player)
        advanceUntilIdle()

        assertFalse(vm.playAudio())
        assertEquals(0, player.playCallCount)
        assertNotNull(vm.state.value.playError)
    }

    @Test
    fun `playErrorShown clears the error`() = runTest {
        val vm = viewModel("2")
        advanceUntilIdle()

        vm.playAudio()
        vm.playErrorShown()

        assertNull(vm.state.value.playError)
    }

    // ─── Mapping ────────────────────────────────

    @Test
    fun `toEpisode carries headline, audio and cover`() {
        val episode = withAudio.toEpisode()

        assertEquals("1", episode.id)
        assertEquals("အသံပါ သတင်း", episode.title)
        assertEquals("https://example.com/1.mp3", episode.audioUrl)
        assertEquals("https://example.com/1.jpg", episode.coverUrl)
    }

    @Test
    fun `toEpisode maps missing audio to blank so PlayerQueue drops it`() {
        assertEquals("", noAudio.toEpisode().audioUrl)
    }
}
