package com.example.myancast.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow                    // ← ★ ဒါ ရှိရမယ် ★

@Dao
interface LibraryDao {

    @Query("SELECT id FROM library WHERE type = 'sub'")
    fun getSubscribedIds(): Flow<List<String>>        // ← Flow လိုတယ်

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LibraryEntity)

    @Query("DELETE FROM library WHERE id = :id")
    suspend fun delete(id: String)
}