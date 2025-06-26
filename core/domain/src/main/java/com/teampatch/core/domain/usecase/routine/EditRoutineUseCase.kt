package com.teampatch.core.domain.usecase.routine

import com.teampatch.core.domain.repository.RoutineRepository
import java.time.Period
import javax.inject.Inject

class EditRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository,
    //    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(routineId: String, name: String, period: Period) {
        routineRepository.editRoutine(routineId = routineId, routineName = name, period = period)
        // todoRepo.edit()
    }
}