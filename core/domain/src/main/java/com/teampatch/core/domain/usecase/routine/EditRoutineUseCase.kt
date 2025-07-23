package com.teampatch.core.domain.usecase.routine

import com.teampatch.core.domain.entities.RoutineAlarmManager
import com.teampatch.core.domain.model.user.User
import com.teampatch.core.domain.repository.RoutineRepository
import com.teampatch.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

class EditRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val userRepository: UserRepository,
    private val routineAlarmManager: RoutineAlarmManager
) {
    suspend operator fun invoke(
        routineId: String,
        name: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime
    ) {
        val user: User = userRepository.getUserInfo().first()

        routineAlarmManager.cancelRoutineAlarm(routineId = routineId.toInt(), daysOfWeekPeriod)

        routineAlarmManager.setRoutineAlarm(
            routineId = routineId.toInt(),
            name = name,
            daysOfWeekPeriod = daysOfWeekPeriod,
            periodTime = periodTime
        )

        routineRepository.editRoutine(
            routineId = routineId,
            routineName = name,
            daysOfWeekPeriod = daysOfWeekPeriod,
            periodTime = periodTime,
            groupId = user.groupId
        )
    }
}