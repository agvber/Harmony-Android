package com.teampatch.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.teampatch.core.database.model.MemoryCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryCardDao {

    @Query("SELECT * FROM memory_card")
    fun getAllMemoryStorage(): Flow<List<MemoryCardEntity>>

    @Query("SELECT * FROM memory_card WHERE id = :id")
    fun getMemoryStorageById(id: Long): Flow<MemoryCardEntity>

    @Query("SELECT * FROM memory_card WHERE title LIKE '%' || :keyword || '%'")
    fun getMemoryCards(keyword: String): Flow<List<MemoryCardEntity>>

    @Insert(MemoryCardEntity::class)
    suspend fun insertMemoryStorage(memoryCardEntity: MemoryCardEntity)

    @Update(MemoryCardEntity::class)
    suspend fun updateMemoryStorage(memoryCardEntity: MemoryCardEntity)

    @Query("DELETE FROM memory_card WHERE id = :id")
    suspend fun deleteMemoryStorageById(id: Long)
}