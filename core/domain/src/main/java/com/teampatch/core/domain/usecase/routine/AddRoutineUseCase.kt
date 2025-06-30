package com.teampatch.core.domain.usecase.routine

import java.time.DayOfWeek
import com.teampatch.core.domain.repository.RoutineRepository
import java.time.LocalTime
import javax.inject.Inject

class AddRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository,
//    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(
        name: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        routineRepository.addRoutine(
            routineName = name,
            daysOfWeekPeriod = daysOfWeekPeriod,
            periodTime = periodTime
        )
//        todoRepository.addTodo()
    }
}