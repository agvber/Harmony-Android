package com.harmony.core.database.model.multimap

import androidx.room.Embedded
import com.harmony.core.database.model.RoutineEntity
import com.harmony.core.database.model.RoutineLogEntity

data class RoutineWithLog(
    @Embedded val routineEntity: RoutineEntity,
    @Embedded val routineLogEntity: RoutineLogEntity,
)