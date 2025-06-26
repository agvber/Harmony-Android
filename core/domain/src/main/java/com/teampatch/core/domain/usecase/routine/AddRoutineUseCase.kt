package com.teampatch.core.domain.usecase.routine

import com.teampatch.core.domain.repository.RoutineRepository
import java.time.Period
import javax.inject.Inject

class AddRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository,
//    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(name: String, period: Period) {
        routineRepository.addRoutine(routineName = name, period = period)
//        todoRepository.addTodo()
    }
}