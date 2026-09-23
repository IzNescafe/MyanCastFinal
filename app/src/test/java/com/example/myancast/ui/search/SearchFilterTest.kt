package com.example.myancast.ui.search

import com.example.myancast.domain.model.Podcast
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchFilterTest {

    private val podcasts = listOf(
        Podcast(id = "p1", title = "နည်းပညာ စကားဝိုင်း", category = "နည်းပညာ"),
        Podcast(id = "p2", title = "မနက်ခင်း သတင်းလွှာ", category = "သတင်း"),
        Podcast(id = "p3", title = "Myanmar Tech Talk", category = "နည်းပညာ")
    )

    @Test
    fun `blank query returns nothing`() {
        assertTrue(searchPodcasts(podcasts, "").isEmpty())
        assertTrue(searchPodcasts(podcasts, "   ").isEmpty())
    }

    @Test
    fun `finds by title in the middle of the text`() {
        assertEquals(listOf("p1"), searchPodcasts(podcasts, "စကား").map { it.id })
    }

    @Test
    fun `finds by category`() {
        assertEquals(listOf("p1", "p3"), searchPodcasts(podcasts, "နည်းပညာ").map { it.id })
    }

    @Test
    fun `ignores case for latin text`() {
        assertEquals(listOf("p3"), searchPodcasts(podcasts, "tech").map { it.id })
    }

    @Test
    fun `trims the spaces the user typed`() {
        assertEquals(listOf("p3"), searchPodcasts(podcasts, "  Tech  ").map { it.id })
    }

    @Test
    fun `no match gives an empty list`() {
        assertTrue(searchPodcasts(podcasts, "ဘောလုံး").isEmpty())
    }

    @Test
    fun `empty source is safe`() {
        assertTrue(searchPodcasts(emptyList(), "နည်း").isEmpty())
    }
}
