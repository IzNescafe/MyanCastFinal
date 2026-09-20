package com.example.myancast.ui.home

import com.example.myancast.domain.model.Podcast

const val ALL_CATEGORY = "အားလုံး"

fun buildCategories(podcasts: List<Podcast>): List<String> {
    return listOf(ALL_CATEGORY) + podcasts
        .map { it.category }
        .filter { it.isNotBlank() }
        .distinct()
        .sorted()
}

fun filterByCategory(podcasts: List<Podcast>, category: String): List<Podcast> {
    return if (category == ALL_CATEGORY) podcasts
    else podcasts.filter { it.category == category }
}