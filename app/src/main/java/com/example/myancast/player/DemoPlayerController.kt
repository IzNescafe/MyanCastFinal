package com.example.myancast.player

import com.example.myancast.domain.model.PlaybackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Temporary controller — အသံ မထွက်။ B ရဲ့ Media3PlayerController မရသေးခင်
 * A နဲ့ C ရဲ့ code တွေ compile ဖြစ်ဖို့။ Day 4 မှာ ဖျက်။
 */
class DemoPlayerController : PlayerController {
    private val _state = MutableStateFlow(PlaybackState())
    override val state: StateFlow<PlaybackState> = _state.asStateFlow()

    override fun play(queue: PlayerQueue, podcastTitle: String) {
        if (queue.isEmpty) return
        _state.update {
            it.copy(
                queue = queue.episodes,
                currentIndex = queue.startIndex,
                podcastTitle = podcastTitle,
                isPlaying = true,
                positionMs = 0L,
                durationMs = queue.episodes[queue.startIndex].duration * 1000L
            )
        }
    }

    override fun togglePlayPause() {
        _state.update { it.copy(isPlaying = !it.isPlaying) }
    }

    override fun seekTo(positionMs: Long) {
        _state.update { it.copy(positionMs = positionMs) }
    }

    override fun skipBack() {
        _state.update { it.copy(positionMs = (it.positionMs - 15_000L).coerceAtLeast(0L)) }
    }

    override fun skipForward() {
        _state.update { it.copy(positionMs = it.positionMs + 30_000L) }
    }

    override fun next() {
        _state.update {
            if (it.hasNext) it.copy(currentIndex = it.currentIndex + 1, positionMs = 0L)
            else it
        }
    }

    override fun previous() {
        _state.update {
            if (it.hasPrevious) it.copy(currentIndex = it.currentIndex - 1, positionMs = 0L)
            else it
        }
    }

    override fun setSpeed(speed: Float) {
        _state.update { it.copy(speed = speed) }
    }
}