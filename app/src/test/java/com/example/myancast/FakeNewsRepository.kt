// app/src/test/java/com/example/myancast/FakeNewsRepository.kt
package com.example.myancast

import com.example.myancast.data.repository.NewsRepository
import com.example.myancast.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeNewsRepository(
    private val news: List<NewsItem> = emptyList(),
    private val error: Throwable? = null
) : NewsRepository {

    override fun getNews(): Flow<List<NewsItem>> =
        if (error != null) flow { throw error } else flowOf(news)

    override suspend fun getNewsItem(id: String): NewsItem? =
        news.find { it.id == id }
}
