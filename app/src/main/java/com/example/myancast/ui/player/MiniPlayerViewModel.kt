package com.example.myancast.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myancast.domain.model.PlaybackState
import com.example.myancast.player.PlayerController
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MiniPlayerViewModel(
    private val controller: PlayerController
) : ViewModel() {

    val state: StateFlow<MiniPlayerUiState> = controller.state
        .map { it.toMiniPlayerUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MiniPlayerUiState()
        )

    fun togglePlayPause() = controller.togglePlayPause()

    fun next() = controller.next()

    companion object {
        fun factory(controller: PlayerController) = viewModelFactory {
            initializer { MiniPlayerViewModel(controller) }
        }
    }
}

private fun PlaybackState.toMiniPlayerUiState(): MiniPlayerUiState {
    return MiniPlayerUiState(
        visible = hasEpisode,
        title = currentEpisode?.title.orEmpty(),
        subtitle = podcastTitle,
        coverUrl = currentEpisode?.coverUrl.orEmpty(),
        progress = progress,
        isPlaying = isPlaying,
        hasNext = hasNext
    )
}