package com.example.myancast.domain.model

import com.google.firebase.Timestamp

data class Episode(
    val id: String = "",
    val podcastId: String = "",
    val title: String = "",
    val description: String = "",
    val audioUrl: String = "",
    val coverUrl: String = "",
    val duration: Int = 0,
    val publishedAt: Timestamp? = null
)