// data/repository/PodcastRepository.kt
package com.example.myancast.data.repository

import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import kotlinx.coroutines.flow.Flow

interface PodcastRepository {
    fun getPodcasts(): Flow<List<Podcast>>
    fun getEpisodes(podcastId: String): Flow<List<Episode>>
    suspend fun getPodcast(id: String): Podcast?
}