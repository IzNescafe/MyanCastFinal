package com.example.myancast.domain.model

data class NewsItem(
    val id: String = "",
    val headline: String = "",
    val body: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val timestamp: Long = 0L
)