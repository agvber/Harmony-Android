package com.teampatch.core.data.mapper

import com.teampatch.core.data.database.model.UserEntity
import com.teampatch.core.domain.model.User
import com.teampatch.core.data.network.model.user.ProfileResponse

internal fun ProfileResponse.toDomain(): User = User(
    uid = user.userId,
    groupId = user.groupId,
    name = user.nick,
    relation = "",
    profileImageUrl = null,
    role = roleStringMapper(user.permissionId!!)
)

internal fun UserEntity.toDomain(): User = User(
    uid = uid.toString(),
    groupId = groupId?.toInt() ?: -1,
    name = name,
    relation = relation,
    profileImageUrl = profileImageUri,
    role = roleStringMapper(role)
)