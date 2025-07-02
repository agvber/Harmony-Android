package com.teampatch.core.data.repository.local

import com.harmony.core.database.LOCAL_DB_DATE_FORMATTER
import com.harmony.core.database.dao.RoutineDao
import com.harmony.core.database.model.RoutineLogEntity
import com.teampatch.core.data.mapper.RoutineMapper
import com.teampatch.core.domain.model.routine.Routine
import com.teampatch.core.domain.model.TaskProgress
import com.teampatch.core.domain.model.routine.DailyRoutine
import com.teampatch.core.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

internal class LocalRoutineRepositoryImpl @Inject constructor(
    private val routineDao: RoutineDao,
    private val routineMapper: RoutineMapper,
) : RoutineRepository {

    override suspend fun addRoutine(
        groupId: Int,
        routineName: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        routineMapper.buildRoutineEntity(
            routineName = routineName,
            daysOfWeekPeriod = daysOfWeekPeriod,
            periodTime = periodTime,
            groupId = groupId.toLong()
        )
            .let { routineDao.insertAll(it) }
    }

    override suspend fun editRoutine(
        groupId: Int,
        routineId: String,
        routineName: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        routineMapper.buildRoutineEntity(
            routineName = routineName,
            daysOfWeekPeriod = daysOfWeekPeriod,
            periodTime = periodTime,
            groupId = groupId.toLong()
        )
            .let { routineDao.upsertAll(it) }
    }

    override suspend fun checkRoutine(routineId: String, isFinished: Boolean) {
        val now: String = LocalDate.now().format(LOCAL_DB_DATE_FORMATTER)
        val routineLog = routineDao.getRoutineLog(routineId.toLong(), now)

        routineLog.onEach { entities ->
            if (entities.isNotEmpty()) {
                routineDao.upsertAll(entities[0].copy(isFinished = isFinished))
                return@onEach
            }
            routineDao.insertAll(
                RoutineLogEntity(
                    routineId = routineId.toLong(),
                    date = now,
                    isFinished = isFinished
                )
            )
        }
            .first()
    }

    override fun getAllRoutines(): Flow<List<Routine>> {
        return routineDao.getAllRoutines().map { entities ->
            entities.mapNotNull { routineMapper.toDomain(it) }
        }
    }

    override fun getRoutineById(id: String): Flow<Routine> {
        return routineDao.getRoutineById(id.toLong())
            .mapNotNull { routineMapper.toDomain(it) }
    }

    override fun getDailyRoutine(date: LocalDate): Flow<List<DailyRoutine>> {
        val dateServerFormat: String = date.format(LOCAL_DB_DATE_FORMATTER)
        return combine(
            flow = routineDao.getAllRoutines(),
            flow2 = routineDao.getRoutineLogByDate(dateServerFormat)
        ) { routines, routineLogs ->
            routines.mapNotNull { routineEntity ->
                routineMapper.toDomain(
                    routineEntity = routineEntity,
                    routineLogs = routineLogs,
                    date = date
                )
            }
        }
    }

    override fun getDailyRoutineProgress(date: LocalDate): Flow<TaskProgress> {
        val dateStringFormat: String = date.format(LOCAL_DB_DATE_FORMATTER)
        return combine(
            routineDao.getAllRoutines(),
            routineDao.getRoutineLogByDate(dateStringFormat)
        ) { routines, routineLogs ->
            val routinesCount: Int = routines.map { routineMapper.toDomain(it) }
                .count { it?.let { date.dayOfWeek in it.daysOfWeekPeriod } == true }
            val finishedCount: Int = routineLogs.count { it.isFinished }
            TaskProgress(routinesCount, finishedCount)
        }
    }

    override suspend fun deleteRoutine(routineId: String) {
        routineDao.deleteRoutineById(routineId.toLong())
    }
}