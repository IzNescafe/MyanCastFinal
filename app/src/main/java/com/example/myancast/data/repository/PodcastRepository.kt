// data/repository/PodcastRepository.kt
package com.example.myancast.data.repository

import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PodcastRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getPodcasts(): Flow<List<Podcast>> = callbackFlow {
        val registration = db.collection("podcasts")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.toObjects(Podcast::class.java) ?: emptyList()
                trySend(list)
            }
        awaitClose { registration.remove() }
    }

    fun getEpisodes(podcastId: String): Flow<List<Episode>> = callbackFlow {
        val registration = db.collection("episodes")
            .whereEqualTo("podcastId", podcastId)
            .orderBy("publishedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.toObjects(Episode::class.java) ?: emptyList()
                trySend(list)
            }
        awaitClose { registration.remove() }
    }
}