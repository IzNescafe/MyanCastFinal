package com.example.myancast.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room3.Dao
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    @Query("SELECT id FROM library WHERE type = 'sub'")
    fun getSubscribedIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LibraryEntity)

    @Query("DELETE FROM library WHERE id = :id")
    suspend fun delete(id: String)
}