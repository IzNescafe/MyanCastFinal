// data/repository/PodcastRepository.kt
package com.example.myancast.data.repository

import com.example.myancast.data.firebase.snapshotFlow
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow

class PodcastRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getPodcasts(): Flow<List<Podcast>> =
        db.collection(COLLECTION_PODCASTS)
            .snapshotFlow(Podcast::class.java) { podcast, id -> podcast.copy(id = id) }

    fun getEpisodes(podcastId: String): Flow<List<Episode>> =
        db.collection(COLLECTION_EPISODES)
            .whereEqualTo("podcastId", podcastId)
            .orderBy("publishedAt", Query.Direction.DESCENDING)
            .snapshotFlow(Episode::class.java) { episode, id -> episode.copy(id = id) }

    private companion object {
        const val COLLECTION_PODCASTS = "podcasts"
        const val COLLECTION_EPISODES = "episodes"
    }
}
