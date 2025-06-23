package com.teampatch.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.teampatch.core.data.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE uid > 0")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE uid = :uid")
    fun getUserById(uid: Long): Flow<UserEntity>

    @Query("SELECT * FROM user WHERE group_id = :id")
    fun getUserByGroupId(id: Long): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE sns_id = :id")
    fun getUserBySnsId(id: String): Flow<UserEntity>

    @Insert
    suspend fun insertUsers(vararg userEntity: UserEntity): List<Long>

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Query("DELETE FROM user WHERE uid = :uid")
    fun deleteUser(uid: Long): Int
}