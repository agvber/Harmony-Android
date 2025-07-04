package com.teampatch.core.data.repository.local

import com.harmony.core.database.LOCAL_DB_DATE_FORMATTER
import com.harmony.core.database.LOCAL_DB_TIME_FORMATTER
import com.harmony.core.database.dao.RoutineDao
import com.harmony.core.database.model.RoutineEntity
import com.harmony.core.database.model.RoutineLogEntity
import com.teampatch.core.common.set
import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.domain.model.TaskProgress
import com.teampatch.core.domain.model.routine.DailyRoutine
import com.teampatch.core.domain.model.routine.Routine
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
) : RoutineRepository {

    override suspend fun addRoutine(
        groupId: Int,
        routineName: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        val routineEntity = RoutineEntity(
            id = null,
            groupId = groupId.toLong(),
            name = routineName,
            dayOfWeek = daysOfWeekPeriod.set { it.name },
            time = periodTime.format(LOCAL_DB_TIME_FORMATTER)
        )
        routineDao.insertAll(routineEntity)
    }

    override suspend fun editRoutine(
        groupId: Int,
        routineId: String,
        routineName: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        val routineEntity = RoutineEntity(
            id = routineId.toLong(),
            groupId = groupId.toLong(),
            name = routineName,
            dayOfWeek = daysOfWeekPeriod.set { it.name },
            time = periodTime.format(LOCAL_DB_TIME_FORMATTER)
        )
        routineDao.upsertAll(routineEntity)
    }

    override suspend fun checkRoutine(routineId: String, isFinished: Boolean) {
        val now: String = LocalDate.now().format(LOCAL_DB_DATE_FORMATTER)
        val routineLog = routineDao.getRoutineLog(routineId.toLong(), now)

        routineLog.onEach { entities ->
            if (entities.isNotEmpty()) {
                routineDao.upsertAll(entities[0].copy(isFinished = isFinished))
                return@onEach
            }
            val routineLogEntity = RoutineLogEntity(
                routineId = routineId.toLong(),
                date = now,
                isFinished = isFinished
            )
            routineDao.insertAll(routineLogEntity)
        }
            .first()
    }

    override fun getAllRoutines(): Flow<List<Routine>> {
        return routineDao.getAllRoutines().map { entities ->
            entities.mapNotNull { it.toDomain() }
        }
    }

    override fun getRoutineById(id: String): Flow<Routine> {
        return routineDao.getRoutineById(id.toLong())
            .mapNotNull { it.toDomain() }
    }

    override fun getDailyRoutine(date: LocalDate): Flow<List<DailyRoutine>> {
        val dateServerFormat: String = date.format(LOCAL_DB_DATE_FORMATTER)
        return combine(
            flow = routineDao.getAllRoutines(),
            flow2 = routineDao.getRoutineLogByDate(dateServerFormat)
        ) { routines, routineLogs ->
            routines.mapNotNull { routineEntity ->
                runCatching {
                    DailyRoutine(
                        routineId = routineEntity.id.toString(),
                        name = routineEntity.name,
                        time = LocalTime.parse(routineEntity.time, LOCAL_DB_TIME_FORMATTER),
                        isFinished = routineLogs.find {
                            it.routineId == routineEntity.id?.toLong()
                        }?.isFinished == true
                    )
                }
                    .onFailure { it.printStackTrace() }
                    .getOrNull()
            }
        }
    }

    override fun getDailyRoutineProgress(date: LocalDate): Flow<TaskProgress> {
        val dateStringFormat: String = date.format(LOCAL_DB_DATE_FORMATTER)
        return combine(
            routineDao.getAllRoutines(),
            routineDao.getRoutineLogByDate(dateStringFormat)
        ) { routines, routineLogs ->
            val routinesCount: Int = routines
                .count { date.dayOfWeek.name in it.dayOfWeek }
            val finishedCount: Int = routineLogs.count { it.isFinished }
            TaskProgress(routinesCount, finishedCount)
        }
    }

    override suspend fun deleteRoutine(routineId: String) {
        routineDao.deleteRoutineById(routineId.toLong())
    }
}