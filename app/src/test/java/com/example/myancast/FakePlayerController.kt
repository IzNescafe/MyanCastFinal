package com.example.myancast

import com.example.myancast.domain.model.PlaybackState
import com.example.myancast.player.PlayerController
import com.example.myancast.player.PlayerQueue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Test အတွက် — call တွေကို record လုပ်တယ်။ A နဲ့ C ရဲ့ test တွေ သုံး။
 */
class FakePlayerController(
    initial: PlaybackState = PlaybackState()
) : PlayerController {

    private val _state = MutableStateFlow(initial)
    override val state: StateFlow<PlaybackState> = _state.asStateFlow()

    // Recorded calls
    var playCallCount = 0
        private set
    var lastQueue: PlayerQueue? = null
        private set
    var lastPodcastTitle: String? = null
        private set
    var toggleCallCount = 0
        private set
    var seekToMs: Long? = null
        private set
    var lastSpeed: Float? = null
        private set

    override fun play(queue: PlayerQueue, podcastTitle: String) {
        playCallCount++
        lastQueue = queue
        lastPodcastTitle = podcastTitle
        if (queue.isEmpty) return
        _state.update {
            it.copy(
                queue = queue.episodes,
                currentIndex = queue.startIndex,
                podcastTitle = podcastTitle,
                isPlaying = true
            )
        }
    }

    override fun togglePlayPause() {
        toggleCallCount++
        _state.update { it.copy(isPlaying = !it.isPlaying) }
    }

    override fun seekTo(positionMs: Long) {
        seekToMs = positionMs
        _state.update { it.copy(positionMs = positionMs) }
    }

    override fun skipBack() { /* Record if needed */ }
    override fun skipForward() { /* Record if needed */ }
    override fun next() { /* Record if needed */ }
    override fun previous() { /* Record if needed */ }

    override fun setSpeed(speed: Float) {
        lastSpeed = speed
        _state.update { it.copy(speed = speed) }
    }

    // ★ Test အတွက် helper — state ကို ပြောင်း
    fun setState(newState: PlaybackState) {
        _state.value = newState
    }
}