package com.example.myancast.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library")
data class LibraryEntity(
    @PrimaryKey val id: String,
    val type: String   // "sub" or "history"
)