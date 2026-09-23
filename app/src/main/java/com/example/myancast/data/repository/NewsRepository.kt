// data/repository/NewsRepository.kt
package com.example.myancast.data.repository

import com.example.myancast.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow

/**
 * သတင်း data source — `PodcastRepository` နဲ့ ပုံစံတူ interface ခွဲထားတယ်။
 * ViewModel က Firestore ကို မသိဘူး၊ ဒီ interface ကိုပဲ သိတယ် (test မှာ fake ထည့်လို့ရ)။
 */
interface NewsRepository {
    fun getNews(): Flow<List<NewsItem>>
    suspend fun getNewsItem(id: String): NewsItem?
}
