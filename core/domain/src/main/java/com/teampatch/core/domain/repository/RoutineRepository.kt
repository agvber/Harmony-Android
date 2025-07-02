package com.teampatch.core.domain.repository

import java.time.DayOfWeek
import com.teampatch.core.domain.model.Routine
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

interface RoutineRepository {

    suspend fun addRoutine(
        groupId: Int,
        routineName: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    )

    suspend fun editRoutine(
        groupId: Int,
        routineId: String,
        routineName: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    )

    suspend fun checkRoutine(routineId: String, isFinished: Boolean)

    fun getAllRoutines(): Flow<List<Routine>>

    fun getRoutineById(id: String): Flow<Routine>

    suspend fun deleteRoutine(routineId: String)
}