package com.teampatch.core.domain.model

import java.time.LocalTime
import java.time.DayOfWeek

data class Routine(
    val id: String,
    val name: String,
    val daysOfWeekPeriod: Set<DayOfWeek>,
    val periodTime: LocalTime
)