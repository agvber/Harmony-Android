package com.teampatch.core.domain.usecase.routine

import com.teampatch.core.domain.model.DayOfWeek
import com.teampatch.core.domain.repository.RoutineRepository
import java.time.LocalTime
import javax.inject.Inject

class EditRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository,
    //    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(
        routineId: String, name: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        routineRepository.editRoutine(
            routineId = routineId,
            routineName = name,
            daysOfWeekPeriod = daysOfWeekPeriod,
            periodTime = periodTime
        )
        // todoRepo.edit()
    }
}