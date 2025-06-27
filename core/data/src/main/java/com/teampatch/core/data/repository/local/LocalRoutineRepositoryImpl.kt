package com.teampatch.core.data.repository.local

import com.harmony.core.database.dao.RoutineDao
import com.harmony.core.database.model.RoutineEntity
import com.teampatch.core.data.mapper.RoutineMapper
import com.teampatch.core.domain.model.DayOfWeek
import com.teampatch.core.domain.model.Routine
import com.teampatch.core.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import javax.inject.Inject

internal class LocalRoutineRepositoryImpl @Inject constructor(
    private val routineDao: RoutineDao,
    private val routineMapper: RoutineMapper
) : RoutineRepository {

    override suspend fun addRoutine(
        routineName: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        val routineEntity: RoutineEntity =
            routineMapper.buildRoutineEntity(routineName, daysOfWeekPeriod, periodTime)
        routineDao.insertAll(routineEntity)
    }

    override suspend fun editRoutine(
        routineId: String,
        routineName: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        val routineEntity: RoutineEntity = routineMapper.buildRoutineEntity(
            routineName,
            daysOfWeekPeriod,
            periodTime,
            routineId.toLong()
        )
        routineDao.upsertAll(routineEntity)
    }

    override fun getAllRoutines(): Flow<List<Routine>> {
        return routineDao.getAllRoutines().map { entities ->
            entities.map { routineMapper.toDomain(it) }
        }
    }

    override fun getRoutineById(id: String): Flow<Routine> {
        return routineDao.getRoutineById(id.toLong())
            .map { routineMapper.toDomain(it) }
    }

    override suspend fun deleteRoutine(routineId: String) {
        routineDao.deleteById(routineId.toLong())
    }
}