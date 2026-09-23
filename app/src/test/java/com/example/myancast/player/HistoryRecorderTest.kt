package com.example.myancast.player

import com.example.myancast.FakeLibraryRepository
import com.example.myancast.FakePlayerController
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.PlaybackState
import com.example.myancast.data.repository.LibraryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryRecorderTest {

    private val episode = Episode(
        id = "e1",
        podcastId = "p1",
        title = "အပိုင်း ၁",
        coverUrl = "cover.jpg"
    )
    private val episode2 = episode.copy(id = "e2", title = "အပိုင်း ၂")

    /**
     * Recorder ကို Unconfined dispatcher နဲ့ စတင်တယ် — state တစ်ခု ပြောင်းတာနဲ့
     * collector က ချက်ချင်း လုပ်တယ် (advanceUntilIdle စောင့်စရာ မလို)။
     * `backgroundScope` ကနေ context ယူထားလို့ test ပြီးရင် အလိုလို cancel ဖြစ်တယ်။
     */
    private fun TestScope.startRecorder(
        player: FakePlayerController,
        library: LibraryRepository,
        now: () -> Long = { 1L }
    ) {
        HistoryRecorder(
            player = player,
            library = library,
            scope = CoroutineScope(
                backgroundScope.coroutineContext + UnconfinedTestDispatcher(testScheduler)
            ),
            now = now
        ).start()
    }

    private fun playing(
        ep: Episode = episode,
        positionMs: Long = 0L,
        durationMs: Long = 100_000L,
        isPlaying: Boolean = true
    ) = PlaybackState(
        queue = listOf(ep),
        currentIndex = 0,
        podcastTitle = "နည်းပညာ",
        isPlaying = isPlaying,
        positionMs = positionMs,
        durationMs = durationMs
    )

    @Test
    fun `saves the episode and podcast details`() = runTest {
        val player = FakePlayerController()
        val library = FakeLibraryRepository()
        startRecorder(player, library, now = { 777L })

        player.setState(playing(positionMs = 3_000L))

        assertEquals(1, library.saved.size)
        val saved = library.saved.first()
        assertEquals("e1", saved.episodeId)
        assertEquals("p1", saved.podcastId)
        assertEquals("အပိုင်း ၁", saved.episodeTitle)
        assertEquals("နည်းပညာ", saved.podcastTitle)
        assertEquals("cover.jpg", saved.coverUrl)
        assertEquals(3_000L, saved.positionMs)
        assertEquals(777L, saved.updatedAt)
    }

    @Test
    fun `does not save while the duration is unknown`() = runTest {
        val player = FakePlayerController()
        val library = FakeLibraryRepository()
        startRecorder(player, library)

        player.setState(playing(positionMs = 2_000L, durationMs = 0L))

        assertTrue(library.saved.isEmpty())
    }

    @Test
    fun `throttles to one save per 10 seconds`() = runTest {
        val player = FakePlayerController()
        val library = FakeLibraryRepository()
        startRecorder(player, library)

        // ၅၀၀ms တစ်ခါ ထွက်တဲ့ state ၅ ခု — အကုန် ၁၀ စက္ကန့် bucket အတွင်း
        listOf(0L, 500L, 1_000L, 1_500L, 2_000L).forEach {
            player.setState(playing(positionMs = it))
        }

        assertEquals(1, library.saved.size)

        // ၁၀ စက္ကန့် ကျော်ရင် နောက်တစ်ခါ သိမ်း
        player.setState(playing(positionMs = 11_000L))

        assertEquals(2, library.saved.size)
        assertEquals(11_000L, library.saved.last().positionMs)
    }

    @Test
    fun `saves again when playback is paused`() = runTest {
        val player = FakePlayerController()
        val library = FakeLibraryRepository()
        startRecorder(player, library)

        player.setState(playing(positionMs = 4_000L))
        assertEquals(1, library.saved.size)

        // Bucket မပြောင်းပေမဲ့ pause ဖြစ်သွားလို့ ချက်ချင်း သိမ်းရမယ်
        player.setState(playing(positionMs = 4_500L, isPlaying = false))

        assertEquals(2, library.saved.size)
        assertEquals(4_500L, library.saved.last().positionMs)
    }

    @Test
    fun `saves immediately when the episode changes`() = runTest {
        val player = FakePlayerController()
        val library = FakeLibraryRepository()
        startRecorder(player, library)

        player.setState(playing(positionMs = 1_000L))

        // Episode အသစ် — position က bucket တူပေမဲ့ သိမ်းရမယ်
        player.setState(playing(ep = episode2, positionMs = 0L))

        assertEquals(2, library.saved.size)
        assertEquals("e2", library.saved.last().episodeId)
    }
}
