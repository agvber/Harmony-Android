package com.teampatch.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "routine_log",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routine_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class RoutineLogEntity(
    @PrimaryKey(autoGenerate = true) val routineLogId: Long? = null,
    @ColumnInfo(name = "routine_id") val routineId: Long,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "is_finished") val isFinished: Boolean,
)