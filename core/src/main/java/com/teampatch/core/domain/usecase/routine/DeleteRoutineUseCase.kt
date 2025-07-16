package com.teampatch.core.domain.usecase.routine

import com.teampatch.core.domain.repository.RoutineRepository
import javax.inject.Inject

class DeleteRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    suspend operator fun invoke(id: String) {
        // if !isFinished
        // todo_repo.delete(today)
        return routineRepository.deleteRoutine(id)
    }
}