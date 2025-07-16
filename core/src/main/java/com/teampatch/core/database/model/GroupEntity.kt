package com.teampatch.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "group", indices = [Index(value = ["invite_code"], unique = true)])
data class GroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "vip_uid") val vipUid: Long?,
    @ColumnInfo(name = "manager_uid") val managerUid: Long?,
    @ColumnInfo(name = "invite_code") val inviteCode: String,
)