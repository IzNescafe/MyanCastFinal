package com.example.myancast

import com.example.myancast.data.repository.LibraryRepository
import com.example.myancast.domain.model.PlaybackProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeLibraryRepository(
    initialSubscribed: Set<String> = emptySet(),
    initialHistory: List<PlaybackProgress> = emptyList()
) : LibraryRepository {

    private val _subscribed = MutableStateFlow(initialSubscribed)
    private val _history = MutableStateFlow(initialHistory)

    override fun subscribedIds(): Flow<Set<String>> = _subscribed

    override suspend fun subscribe(podcastId: String) {
        _subscribed.value = _subscribed.value + podcastId
    }

    override suspend fun unsubscribe(podcastId: String) {
        _subscribed.value = _subscribed.value - podcastId
    }

    override fun history(): Flow<List<PlaybackProgress>> = _history

    override fun lastPlayed(): Flow<PlaybackProgress?> =
        _history.map { it.maxByOrNull { p -> p.updatedAt } }

    override suspend fun saveProgress(progress: PlaybackProgress) {
        _history.value = _history.value.filter { it.episodeId != progress.episodeId } + progress
    }
}