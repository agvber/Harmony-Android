package com.teampatch.core.data.repository.local

import com.harmony.core.database.dao.RoutineDao
import com.teampatch.core.data.mapper.RoutineMapper
import com.teampatch.core.domain.model.Routine
import com.teampatch.core.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

internal class LocalRoutineRepositoryImpl @Inject constructor(
    private val routineDao: RoutineDao,
    private val routineMapper: RoutineMapper
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

    override fun getAllRoutines(): Flow<List<Routine>> {
        return routineDao.getAllRoutines().map { entities ->
            entities.mapNotNull { routineMapper.toDomain(it) }
        }
    }

    override fun getRoutineById(id: String): Flow<Routine> {
        return routineDao.getRoutineById(id.toLong())
            .mapNotNull { routineMapper.toDomain(it) }
    }

    override suspend fun deleteRoutine(routineId: String) {
        routineDao.deleteById(routineId.toLong())
    }
}