package com.harmony.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.harmony.core.database.model.RoutineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {

    @Query("SELECT * FROM routine")
    fun getAllRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routine WHERE id = :id")
    fun getRoutineById(id: Long): Flow<RoutineEntity>

    @Insert
    suspend fun insertAll(vararg routines: RoutineEntity): List<Long>

    @Update(entity = RoutineEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(vararg routine: RoutineEntity)

    @Query("DELETE FROM routine WHERE id = :id")
    suspend fun deleteById(id: Long)
}