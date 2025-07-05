package com.teampatch.core.domain.usecase.routine

import com.teampatch.core.domain.model.routine.Routine
import com.teampatch.core.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoutinesUseCase @Inject constructor(
    private val routineRepository: RoutineRepository,
) {

    operator fun invoke(): Flow<List<Routine>> {
        return routineRepository.getAllRoutines()
    }
}