package com.harmony.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.harmony.core.database.model.RoutineEntity
import com.harmony.core.database.model.RoutineLogEntity
import com.harmony.core.database.model.multimap.RoutineWithLog
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {

    @Query("SELECT * FROM routine ORDER BY time ASC")
    fun getAllRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routine WHERE id = :id")
    fun getRoutineById(id: Long): Flow<RoutineEntity>

    @Query(
        "SELECT * FROM routine " +
                "INNER JOIN routine_log ON routine.id = routine_log.routine_id " +
                "WHERE date(routine_log.date) = :date"
    )
    fun getAllRoutineWithLog(date: String): Flow<List<RoutineWithLog>>

    @Query("SELECT * FROM routine_log WHERE routine_log.routine_id = :routineId AND date(routine_log.date) = date(:date)")
    fun getRoutineLog(routineId: Long, date: String): Flow<List<RoutineLogEntity>>

    @Query("SELECT * FROM routine_log WHERE date(date) = date(:date)")
    fun getRoutineLogByDate(date: String): Flow<List<RoutineLogEntity>>

    @Insert(entity = RoutineEntity::class)
    suspend fun insertAll(vararg routines: RoutineEntity): List<Long>

    @Insert(entity = RoutineLogEntity::class)
    suspend fun insertAll(vararg routineLogs: RoutineLogEntity): List<Long>

    @Update(entity = RoutineEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(vararg routine: RoutineEntity)

    @Update(entity = RoutineLogEntity::class)
    suspend fun upsertAll(vararg routineLogs: RoutineLogEntity)

    @Query("UPDATE routine_log SET is_finished = :isFinished WHERE routineLogId = :routineLogId")
    suspend fun updateRoutine(routineLogId: Long, isFinished: Boolean)

    @Query("DELETE FROM routine WHERE id = :id")
    suspend fun deleteRoutineById(id: Long)

    @Query("DELETE FROM routine_log WHERE routineLogId = :id")
    suspend fun deleteRoutineLogById(id: Long)
}