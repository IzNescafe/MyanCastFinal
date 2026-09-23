// data/repository/FirestoreNewsRepository.kt
package com.example.myancast.data.repository

import android.util.Log
import com.example.myancast.data.firebase.snapshotFlow
import com.example.myancast.data.firebase.toObjectWithId
import com.example.myancast.domain.model.NewsItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class FirestoreNewsRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : NewsRepository {

    /**
     * ⚠️ `orderBy("publishedAt")` ကြောင့် အဲဒီ field မပါတဲ့ document က တိတ်တဆိတ် ပျောက်တယ်
     * (Phase 2 ရဲ့ သင်ခန်းစာ)။ News မပေါ်ရင် Console မှာ `publishedAt` (timestamp) အရင် စစ်ပါ။
     */
    override fun getNews(): Flow<List<NewsItem>> =
        db.collection(COLLECTION_NEWS)
            .orderBy("publishedAt", Query.Direction.DESCENDING)
            .limit(NEWS_LIMIT)
            .snapshotFlow(NewsItem::class.java) { news, id -> news.copy(id = id) }

    override suspend fun getNewsItem(id: String): NewsItem? {
        return try {
            val snapshot = db.collection(COLLECTION_NEWS)
                .document(id)
                .get()
                .await()

            if (snapshot.exists()) {
                snapshot.toObjectWithId(NewsItem::class.java) { news, docId ->
                    news.copy(id = docId)
                }
            } else {
                Log.d("FirestoreNewsRepository", "getNewsItem: Document $id not found")
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreNewsRepository", "getNewsItem failed: ${e.message}", e)
            null
        }
    }

    private companion object {
        const val COLLECTION_NEWS = "news"
        const val NEWS_LIMIT = 50L
    }
}
