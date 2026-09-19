// domain/model/NewsItem.kt
package com.example.myancast.domain.model

import com.google.firebase.Timestamp

data class NewsItem(
    val id: String = "",
    val headline: String = "",
    val body: String = "",
    val imageUrl: String = "",
    val audioUrl: String? = null,
    val category: String = "",
    val publishedAt: Timestamp? = null
)
