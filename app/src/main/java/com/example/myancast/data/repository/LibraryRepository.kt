package com.example.myancast.data.repository

import com.example.myancast.domain.model.PlaybackProgress
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    // ─── Subscribe (A) ───
    fun subscribedIds(): Flow<Set<String>>
    suspend fun subscribe(podcastId: String)
    suspend fun unsubscribe(podcastId: String)

    // ─── History (C) ───
    fun history(): Flow<List<PlaybackProgress>>
    fun lastPlayed(): Flow<PlaybackProgress?>
    suspend fun saveProgress(progress: PlaybackProgress)
}