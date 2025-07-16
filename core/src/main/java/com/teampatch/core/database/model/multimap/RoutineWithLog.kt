package com.teampatch.core.database.model.multimap

import androidx.room.Embedded
import com.teampatch.core.database.model.RoutineEntity
import com.teampatch.core.database.model.RoutineLogEntity

data class RoutineWithLog(
    @Embedded val routineEntity: RoutineEntity,
    @Embedded val routineLogEntity: RoutineLogEntity,
)