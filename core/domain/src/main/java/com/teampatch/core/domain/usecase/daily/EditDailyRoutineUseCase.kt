package com.teampatch.core.domain.usecase.daily

import com.teampatch.core.domain.repository.TodoRepository
import com.teampatch.core.domain.repository.UserRepository
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

class EditDailyRoutineUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val todoRepository: TodoRepository
) {

    suspend operator fun invoke(
        dailyRoutineId: String,
        dailyRoutineTitle: String,
        dayOfWeek: Set<DayOfWeek>,
        time: LocalTime
    ) {
        userRepository.getUserInfo()

    }
}