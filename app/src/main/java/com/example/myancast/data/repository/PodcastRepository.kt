// data/repository/PodcastRepository.kt
package com.example.myancast.data.repository

import android.util.Log
import com.example.myancast.data.firebase.snapshotFlow
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class PodcastRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun getPodcasts(): Flow<List<Podcast>> =
        db.collection(COLLECTION_PODCASTS)
            .snapshotFlow(Podcast::class.java) { podcast, _ ->
                podcast   // ← copy မလို — Firestore id field က auto-map
            }

    fun getEpisodes(podcastId: String): Flow<List<Episode>> =
        db.collection(COLLECTION_EPISODES)
            .whereEqualTo("podcastId", podcastId)
            .orderBy("publishedAt", Query.Direction.DESCENDING)
            .snapshotFlow(Episode::class.java) { episode, _ ->
                episode   // ← copy မလို
            }

    suspend fun getPodcast(id: String): Podcast? {
        return try {
            val snapshot = db.collection(COLLECTION_PODCASTS)
                .document(id)
                .get()
                .await()

            if (snapshot.exists()) {
                snapshot.toObject(Podcast::class.java)
            } else {
                Log.d("PodcastRepository", "getPodcast: Document $id not found")
                null
            }
        } catch (e: Exception) {
            Log.e("PodcastRepository", "getPodcast failed: ${e.message}", e)
            null
        }
    }

    private companion object {
        const val COLLECTION_PODCASTS = "podcasts"
        const val COLLECTION_EPISODES = "episodes"
    }
}