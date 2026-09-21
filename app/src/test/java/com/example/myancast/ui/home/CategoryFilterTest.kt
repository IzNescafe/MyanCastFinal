package com.example.myancast.ui.home

import com.example.myancast.domain.model.Podcast
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * `CategoryFilter.kt` ရဲ့ pure function ၂ ခုအတွက် test —
 * `buildCategories()` နဲ့ `filterByCategory()`။
 */
class CategoryFilterTest {

    private val podcasts = listOf(
        Podcast(id = "1", category = "သတင်း"),
        Podcast(id = "2", category = "နည်းပညာ"),
        Podcast(id = "3", category = "သတင်း"),
        Podcast(id = "4", category = ""),           // blank category — ဖယ်ရမယ်
        Podcast(id = "5", category = "ကျန်းမာရေး")
    )

    // ─────────────────────────────────────────────
    //  buildCategories()
    // ─────────────────────────────────────────────

    @Test
    fun `categories start with ALL, no blanks, no duplicates`() {
        val result = buildCategories(podcasts)

        // ပထမဆုံး က ALL_CATEGORY ဖြစ်ရမယ်
        assertEquals(ALL_CATEGORY, result.first())

        // blank category ဖယ်ရမယ်
        assertTrue("Blank category should be filtered out", "" !in result)

        // ထပ်တာ ဖယ်ရမယ် — "သတင်း" ၂ ခါ ပါပေမဲ့ ၁ ခါပဲ
        val withoutAll = result.drop(1)
        assertEquals(withoutAll.size, withoutAll.distinct().size)

        // ALL + သတင်း + နည်းပညာ + ကျန်းမာရေး = ၄
        assertEquals(4, result.size)
    }

    @Test
    fun `empty list gives only ALL`() {
        assertEquals(listOf(ALL_CATEGORY), buildCategories(emptyList()))
    }

    @Test
    fun `categories are sorted alphabetically`() {
        val result = buildCategories(podcasts).drop(1)
        assertEquals(result.sorted(), result)
    }

    @Test
    fun `single podcast yields two entries`() {
        val one = listOf(Podcast(id = "x", category = "သတင်း"))
        assertEquals(listOf(ALL_CATEGORY, "သတင်း"), buildCategories(one))
    }

    // ─────────────────────────────────────────────
    //  filterByCategory()
    // ─────────────────────────────────────────────

    @Test
    fun `ALL returns everything`() {
        val result = filterByCategory(podcasts, ALL_CATEGORY)
        assertEquals(podcasts.size, result.size)
        assertEquals(podcasts, result)   // order လည်း မပြောင်းရ
    }

    @Test
    fun `filter by category returns matching items only`() {
        val news = filterByCategory(podcasts, "သတင်း")
        assertEquals(2, news.size)
        assertEquals(listOf("1", "3"), news.map { it.id })
        assertTrue(news.all { it.category == "သတင်း" })
    }

    @Test
    fun `unknown category gives empty list`() {
        val result = filterByCategory(podcasts, "မရှိတဲ့ category")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `filter on empty list is safe`() {
        assertTrue(filterByCategory(emptyList(), ALL_CATEGORY).isEmpty())
        assertTrue(filterByCategory(emptyList(), "သတင်း").isEmpty())
    }

    @Test
    fun `blank category is not returned by normal filter`() {
        // "" category က ALL မှာ ပါပေမဲ့ "သတင်း" filter မှာ မပါရ
        val news = filterByCategory(podcasts, "သတင်း")
        assertTrue(news.none { it.category.isBlank() })
    }
}