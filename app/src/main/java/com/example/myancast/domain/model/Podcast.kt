package com.example.myancast.domain.model

data class Podcast(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val coverUrl: String = "",
    val category: String = "",
    val episodeCount: Int = 0
)