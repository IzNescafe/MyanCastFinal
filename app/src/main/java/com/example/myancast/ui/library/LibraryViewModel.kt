package com.example.myancast.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myancast.data.repository.LibraryRepository
import com.example.myancast.data.repository.PodcastRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LibraryViewModel(
    private val libraryRepo: LibraryRepository,
    private val podcastRepo: PodcastRepository
) : ViewModel() {

    private val _tab = MutableStateFlow(LibraryTab.SUBSCRIBED)
    val tab: StateFlow<LibraryTab> = _tab.asStateFlow()

    val state: StateFlow<LibraryUiState> = combine(
        _tab,
        podcastRepo.getPodcasts(),
        libraryRepo.subscribedIds(),
        libraryRepo.history()
    ) { tab, podcasts, subscribedIds, history ->
        LibraryUiState(
            tab = tab,
            subscribed = podcasts.filter { it.id in subscribedIds },
            history = history.map { progress ->
                HistoryRow(
                    episodeId = progress.episodeId,
                    episodeTitle = progress.episodeTitle,
                    podcastId = progress.podcastId,
                    podcastTitle = progress.podcastTitle,
                    coverUrl = progress.coverUrl,
                    progress = progress
                )
            },
            isLoading = false,
            error = null
        )
    }
        .catch { e ->
            emit(
                LibraryUiState(
                    isLoading = false,
                    error = e.message ?: "Data ဆွဲမရပါ"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LibraryUiState(isLoading = true)
        )

    fun selectTab(tab: LibraryTab) {
        _tab.update { tab }
    }

    companion object {
        fun factory(
            libraryRepo: LibraryRepository,
            podcastRepo: PodcastRepository
        ) = viewModelFactory {
            initializer { LibraryViewModel(libraryRepo, podcastRepo) }
        }
    }
}