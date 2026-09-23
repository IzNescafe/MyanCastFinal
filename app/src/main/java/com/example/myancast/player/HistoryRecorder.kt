// player/HistoryRecorder.kt
package com.example.myancast.player

import com.example.myancast.data.repository.LibraryRepository
import com.example.myancast.domain.model.PlaybackProgress
import com.example.myancast.domain.model.PlaybackState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Playback ကို နားထောင်ပြီး "ဘယ်အထိ နားထောင်ပြီးပြီလဲ" ကို Room ထဲ သိမ်းသူ။
 *
 * App တစ်သက်လုံး **တစ်ခုတည်း** (`MyanCastApp`) — ViewModel မဟုတ်ဘူး။
 * Screen ပိတ်သွားလည်း (background playback) ဆက်သိမ်းရမယ်။
 *
 * @param now test မှာ အချိန် ပုံသေ ထားနိုင်ဖို့
 */
class HistoryRecorder(
    private val player: PlayerController,
    private val library: LibraryRepository,
    private val scope: CoroutineScope,
    private val now: () -> Long = System::currentTimeMillis
) {
    private var lastEpisodeId: String? = null
    private var lastBucket = -1L
    private var wasPlaying = false

    fun start() {
        scope.launch {
            player.state.collect { onState(it) }
        }
    }

    private suspend fun onState(state: PlaybackState) {
        val episode = state.currentEpisode

        // Duration မသိသေးရင် (stream load နေဆဲ) မသိမ်းဘူး —
        // သိမ်းမိရင် fraction မှားပြီး isFinished ဖြစ်သွားလို့ Home မှာ မပေါ်တော့ဘူး
        if (episode == null || state.durationMs <= 0L) {
            wasPlaying = state.isPlaying
            return
        }

        // Player state က ၅၀၀ms တစ်ခါ ထွက်တယ် — အကုန် သိမ်းရင် Room write အရမ်းများမယ်။
        // ၁၀ စက္ကန့် "bucket" တစ်ခု ပြောင်းမှ တစ်ခါ သိမ်းတယ်။
        val bucket = state.positionMs / SAVE_EVERY_MS
        val episodeChanged = episode.id != lastEpisodeId
        val justPaused = wasPlaying && !state.isPlaying
        wasPlaying = state.isPlaying

        if (!episodeChanged && !justPaused && bucket == lastBucket) return

        lastEpisodeId = episode.id
        lastBucket = bucket

        library.saveProgress(
            PlaybackProgress(
                episodeId = episode.id,
                podcastId = episode.podcastId,
                positionMs = state.positionMs,
                durationMs = state.durationMs,
                updatedAt = now(),
                episodeTitle = episode.title,
                podcastTitle = state.podcastTitle,
                coverUrl = episode.coverUrl
            )
        )
    }

    private companion object {
        /** ၁၀ စက္ကန့် — battery နဲ့ Room write ကြား အလယ်အလတ် */
        const val SAVE_EVERY_MS = 10_000L
    }
}
