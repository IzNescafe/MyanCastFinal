// data/repository/PodcastRepositoryImpl.kt
package com.example.myancast.data.repository

import android.util.Log
import com.example.myancast.data.firebase.snapshotFlow
import com.example.myancast.data.firebase.toObjectWithId
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class PodcastRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : PodcastRepository {

    override fun getPodcasts(): Flow<List<Podcast>> =
        db.collection(COLLECTION_PODCASTS)
            .snapshotFlow(Podcast::class.java) { podcast, id ->
                podcast.copy(id = id)
            }

    override fun getEpisodes(podcastId: String): Flow<List<Episode>> =
        db.collection(COLLECTION_EPISODES)
            .whereEqualTo("podcastId", podcastId)
            .orderBy("publishedAt", Query.Direction.DESCENDING)
            .snapshotFlow(Episode::class.java) { episode, id ->
                episode.copy(id = id)
            }

    override suspend fun getPodcast(id: String): Podcast? {
        return try {
            val snapshot = db.collection(COLLECTION_PODCASTS)
                .document(id)
                .get()
                .await()

            if (snapshot.exists()) {
                snapshot.toObjectWithId(Podcast::class.java) { podcast, docId ->
                    podcast.copy(id = docId)
                }
            } else {
                Log.d("PodcastRepositoryImpl", "getPodcast: Document $id not found")
                null
            }
        } catch (e: Exception) {
            Log.e("PodcastRepositoryImpl", "getPodcast failed: ${e.message}", e)
            null
        }
    }

    private companion object {
        const val COLLECTION_PODCASTS = "podcasts"
        const val COLLECTION_EPISODES = "episodes"
    }
}