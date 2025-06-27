package com.teampatch.core.domain.repository

import com.teampatch.core.domain.model.DayOfWeek
import com.teampatch.core.domain.model.Routine
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

interface RoutineRepository {

    suspend fun addRoutine(
        routineName: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    )

    suspend fun editRoutine(
        routineId: String,
        routineName: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    )

    fun getAllRoutines(): Flow<List<Routine>>

    fun getRoutineById(id: String): Flow<Routine>

    suspend fun deleteRoutine(routineId: String)
}