package com.example.myancast.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Episode(
    @DocumentId
    val id: String = "",
    val podcastId: String = "",
    val title: String = "",
    val description: String = "",
    val audioUrl: String = "",
    val duration: Int = 0,
    @ServerTimestamp
    val publishedAt: Timestamp? = null,
    val coverUrl: String = ""
) {
    val formattedDuration: String
        get() {
            val minutes = duration / 60
            val seconds = duration % 60
            return "%02d:%02d".format(minutes, seconds)
        }
}