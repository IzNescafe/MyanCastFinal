// ui/news/NewsFilter.kt
package com.example.myancast.ui.news

import com.example.myancast.domain.model.NewsItem
import com.example.myancast.ui.home.ALL_CATEGORY

/**
 * Phase 2 ရဲ့ `CategoryFilter.kt` ပုံစံအတိုင်း — pure function တွေမို့ test လွယ်တယ်။
 * Chip စာသား ("အားလုံး") ကို `ALL_CATEGORY` ကနေပဲ ယူတယ် — နှစ်နေရာ မကွဲအောင်။
 */
fun buildNewsCategories(news: List<NewsItem>): List<String> {
    return listOf(ALL_CATEGORY) + news
        .map { it.category }
        .filter { it.isNotBlank() }
        .distinct()
        .sorted()
}

fun filterNews(news: List<NewsItem>, category: String): List<NewsItem> {
    return if (category == ALL_CATEGORY) news
    else news.filter { it.category == category }
}
