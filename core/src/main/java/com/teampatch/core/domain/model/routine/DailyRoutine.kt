package com.teampatch.core.domain.model.routine

import java.time.LocalTime

data class DailyRoutine(
    val routineId: String,
    val name: String,
    val time: LocalTime,
    val isFinished: Boolean,
)