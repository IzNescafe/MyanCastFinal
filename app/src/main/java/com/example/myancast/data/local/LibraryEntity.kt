package com.example.myancast.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library")
data class LibraryEntity(
    @PrimaryKey val id: String,
    val type: String,  // "sub" or "history"
    val podcastId: String = "",        // history က ဘယ် podcast လဲ
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val updatedAt: Long = 0L
) {
    companion object {
        const val TYPE_SUB = "sub"
        const val TYPE_HISTORY = "history"
    }
}