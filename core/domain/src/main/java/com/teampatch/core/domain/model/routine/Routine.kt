package com.teampatch.core.domain.model.routine

import java.time.DayOfWeek
import java.time.LocalTime

data class Routine(
    val id: String,
    val name: String,
    val daysOfWeekPeriod: Set<DayOfWeek>,
    val periodTime: LocalTime
)