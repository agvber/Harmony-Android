package com.teampatch.core.domain.repository

import com.teampatch.core.domain.model.user.Role
import com.teampatch.core.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserInfo(): Flow<User>

    suspend fun editProfile(
        name: String?,
        profileImageUri: String?,
    )

    suspend fun addUserProfile(
        name: String,
        relation: String,
        profileImageUrl: String?,
        role: Role,
    )
}