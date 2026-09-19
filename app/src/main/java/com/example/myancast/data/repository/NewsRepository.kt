// data/repository/NewsRepository.kt
package com.example.myancast.data.repository

import com.example.myancast.data.firebase.snapshotFlow
import com.example.myancast.domain.model.NewsItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow

class NewsRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getNews(): Flow<List<NewsItem>> =
        db.collection(COLLECTION_NEWS)
            .orderBy("publishedAt", Query.Direction.DESCENDING)
            .limit(NEWS_LIMIT)
            .snapshotFlow(NewsItem::class.java) { news, id -> news.copy(id = id) }

    private companion object {
        const val COLLECTION_NEWS = "news"
        const val NEWS_LIMIT = 50L
    }
}
