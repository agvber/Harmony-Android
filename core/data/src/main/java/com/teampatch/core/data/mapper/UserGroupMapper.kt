package com.teampatch.core.data.mapper

import com.teampatch.core.domain.model.group.FamilyInfo
import com.teampatch.core.domain.model.group.UserGroup
import com.teampatch.core.network.model.group.response.UserGroupListQueryResponse

internal fun UserGroupListQueryResponse.Groups.toDomain(): UserGroup = UserGroup(
    groupId = groupId.toString(),
    name = name,
    members = members.map { member ->
        FamilyInfo(
            title = member.alias ?: "",
            name = member.nick,
            isManager = false,
            role = roleStringMapper(member.permissionId),
            profileImageUrl = member.profile
        )
    }
)