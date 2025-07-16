package com.teampatch.core.data.mapper

import com.teampatch.core.database.LOCAL_DB_TIME_FORMATTER
import com.teampatch.core.database.model.RoutineEntity
import com.teampatch.core.common.set
import com.teampatch.core.domain.model.routine.Routine
import java.time.DayOfWeek
import java.time.LocalTime

fun RoutineEntity.toDomain(): Routine? = runCatching {
    Routine(
        id = id.toString(),
        name = name,
        daysOfWeekPeriod = dayOfWeek.set { DayOfWeek.valueOf(it) },
        periodTime = LocalTime.parse(time, LOCAL_DB_TIME_FORMATTER)
    )
}
    .onFailure { it.printStackTrace() }
    .getOrNull()