package com.teampatch.core.domain.usecase.routine

import com.teampatch.core.domain.repository.RoutineRepository
import javax.inject.Inject

class SetCheckableDailyRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository,
) {
    suspend operator fun invoke(
        id: String,
        isFinished: Boolean,
    ) {
        routineRepository.checkRoutine(id, isFinished)
    }
}