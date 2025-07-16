package com.teampatch.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.teampatch.core.database.model.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Query("SELECT * FROM `group`")
    fun queryGroups(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM `group` WHERE id = :groupId")
    fun queryGroupById(groupId: Long): Flow<GroupEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM `group` WHERE id = :groupId) AS is_present")
    suspend fun queryIsGroupIdPresent(groupId: Long): Boolean

    @Query("SELECT * FROM `group` WHERE invite_code = :inviteCode")
    fun queryGroupByInviteCode(inviteCode: String): Flow<GroupEntity>

    @Insert
    suspend fun insertGroups(vararg groupEntity: GroupEntity): List<Long>

    @Query("DELETE FROM `group` WHERE id = :groupId")
    fun deleteGroupById(groupId: Long)

    @Delete
    fun deleteGroup(groupEntity: GroupEntity)
}