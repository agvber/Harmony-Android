package com.teampatch.core.domain.usecase.routine

import com.teampatch.core.domain.model.routine.Routine
import com.teampatch.core.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    suspend operator fun invoke(id: String): Routine {
        return routineRepository.getRoutineById(id).first()
    }
}