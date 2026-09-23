package com.example.myancast.ui.library

import com.example.myancast.domain.model.PlaybackProgress
import com.example.myancast.domain.model.Podcast

enum class LibraryTab {
    SUBSCRIBED,
    HISTORY
}

data class LibraryUiState(
    val tab: LibraryTab = LibraryTab.SUBSCRIBED,
    val subscribed: List<Podcast> = emptyList(),
    val history: List<HistoryRow> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

data class HistoryRow(
    val episodeId: String = "",
    val episodeTitle: String = "",
    val podcastId: String = "",
    val podcastTitle: String = "",
    val coverUrl: String = "",
    val progress: PlaybackProgress
)