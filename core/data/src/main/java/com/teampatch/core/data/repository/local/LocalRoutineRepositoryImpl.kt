package com.teampatch.core.data.repository.local

import com.harmony.core.database.dao.RoutineDao
import com.harmony.core.database.model.RoutineEntity
import com.teampatch.core.domain.model.Routine
import com.teampatch.core.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Period
import javax.inject.Inject

internal class LocalRoutineRepositoryImpl @Inject constructor(
    private val routineDao: RoutineDao
) : RoutineRepository {

    override suspend fun addRoutine(routineName: String, period: Period) {
        val routineEntity = RoutineEntity(id = null, name = routineName, period = period.toString())
        routineDao.insertAll(routineEntity)
    }

    override suspend fun editRoutine(routineId: String, routineName: String, period: Period) {
        val routineEntity = RoutineEntity(
            id = routineId.toLong(),
            name = routineName,
            period = period.toString()
        )
        routineDao.upsertAll(routineEntity)
    }

    override fun getAllRoutines(): Flow<List<Routine>> {
        return routineDao.getAllRoutines().map { entities ->
            entities.map {
                Routine(id = it.id.toString(), name = it.name, period = Period.parse(it.period))
            }
        }
    }

    override fun getRoutineById(id: String): Flow<Routine> {
        return routineDao.getRoutineById(id.toLong()).map {
            Routine(id = it.id.toString(), name = it.name, period = Period.parse(it.period))
        }
    }

    override suspend fun deleteRoutine(routineId: String) {
        routineDao.deleteById(routineId.toLong())
    }
}