package com.harmony.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val uid: Long? = null,
    @ColumnInfo(name = "group_id") val groupId: Long?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "relation") val relation: String,
    @ColumnInfo(name = "profile_image_uri") val profileImageUri: String?,
    @ColumnInfo(name = "role") val role: String,
    @ColumnInfo(name = "sns_id") val snsId: String,
)