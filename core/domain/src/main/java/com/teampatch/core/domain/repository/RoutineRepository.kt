package com.teampatch.core.domain.repository

import com.teampatch.core.domain.model.Routine
import kotlinx.coroutines.flow.Flow
import java.time.Period

interface RoutineRepository {

    suspend fun addRoutine(
        routineName: String,
        period: Period,
    )

    suspend fun editRoutine(
        routineId: String,
        routineName: String,
        period: Period,
    )

    fun getAllRoutines(): Flow<List<Routine>>

    fun getRoutineById(id: String): Flow<Routine>

    suspend fun deleteRoutine(routineId: String)
}