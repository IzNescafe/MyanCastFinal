// app/src/test/java/com/example/myancast/FakePodcastRepository.kt
package com.example.myancast

import com.example.myancast.data.repository.PodcastRepository
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePodcastRepository(
    private val podcasts: List<Podcast> = emptyList(),
    private val episodes: List<Episode> = emptyList()
) : PodcastRepository {

    override fun getPodcasts(): Flow<List<Podcast>> = flowOf(podcasts)

    override fun getEpisodes(podcastId: String): Flow<List<Episode>> =
        flowOf(episodes.filter { it.podcastId == podcastId })

    override suspend fun getPodcast(id: String): Podcast? =
        podcasts.find { it.id == id }
}