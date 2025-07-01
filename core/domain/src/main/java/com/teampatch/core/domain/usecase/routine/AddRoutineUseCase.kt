package com.teampatch.core.domain.usecase.routine

import com.teampatch.core.domain.repository.RoutineRepository
import com.teampatch.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

class AddRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        name: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        userRepository.getUserInfo()
            .onEach {
                routineRepository.addRoutine(
                    routineName = name,
                    daysOfWeekPeriod = daysOfWeekPeriod,
                    periodTime = periodTime,
                    groupId = it.groupId
                )
            }
            .first()
    }
}