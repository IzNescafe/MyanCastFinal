// app/src/test/java/com/example/myancast/ui/news/NewsFilterTest.kt
package com.example.myancast.ui.news

import com.example.myancast.domain.model.NewsItem
import com.example.myancast.ui.home.ALL_CATEGORY
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsFilterTest {

    private fun news(id: String, category: String) =
        NewsItem(id = id, headline = "ခေါင်းစဉ် $id", category = category)

    private val fakeNews = listOf(
        news("1", "နိုင်ငံရေး"),
        news("2", "အားကစား"),
        news("3", "နိုင်ငံရေး"),
        news("4", "")                       // category မပါတဲ့ document
    )

    @Test
    fun `buildNewsCategories puts all first, no duplicates, no blank`() {
        val categories = buildNewsCategories(fakeNews)

        assertEquals(ALL_CATEGORY, categories.first())
        // `sorted()` က Unicode code point အလိုက် စီတယ် — န (U+1014) က အ (U+1021) ထက် ရှေ့
        assertEquals(listOf(ALL_CATEGORY, "နိုင်ငံရေး", "အားကစား"), categories)
    }

    @Test
    fun `buildNewsCategories on empty list returns only all`() {
        assertEquals(listOf(ALL_CATEGORY), buildNewsCategories(emptyList()))
    }

    @Test
    fun `filterNews with all returns everything`() {
        assertEquals(4, filterNews(fakeNews, ALL_CATEGORY).size)
    }

    @Test
    fun `filterNews with a category returns only that category`() {
        val result = filterNews(fakeNews, "နိုင်ငံရေး")

        assertEquals(2, result.size)
        assertEquals(listOf("1", "3"), result.map { it.id })
    }

    @Test
    fun `filterNews with unknown category returns empty`() {
        assertEquals(emptyList<NewsItem>(), filterNews(fakeNews, "စီးပွားရေး"))
    }
}
