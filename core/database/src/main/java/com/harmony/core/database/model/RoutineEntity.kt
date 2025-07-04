package com.harmony.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "routine",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["group_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "group_id") val groupId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "day_of_week") val dayOfWeek: Set<String>,
    @ColumnInfo(name = "time") val time: String,
)