package com.teampatch.core.domain.entities

import java.time.DayOfWeek
import java.time.LocalTime

interface RoutineAlarmManager {

    fun setRoutineAlarm(
        routineId: Int,
        name: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime,
    )

    fun cancelRoutineAlarm(
        routineId: Int,
        daysOfWeekPeriod: Set<DayOfWeek>,
    )
}