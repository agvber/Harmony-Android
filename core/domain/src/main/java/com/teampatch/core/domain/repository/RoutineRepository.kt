package com.teampatch.core.domain.repository

import com.teampatch.core.domain.model.TaskProgress
import com.teampatch.core.domain.model.routine.DailyRoutine
import com.teampatch.core.domain.model.routine.Routine
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

interface RoutineRepository {

    suspend fun addRoutine(
        groupId: Int,
        routineName: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ): String

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

    fun getDailyRoutine(date: LocalDate): Flow<List<DailyRoutine>>

    fun getDailyRoutineProgress(date: LocalDate): Flow<TaskProgress>

    suspend fun deleteRoutine(routineId: String)
}