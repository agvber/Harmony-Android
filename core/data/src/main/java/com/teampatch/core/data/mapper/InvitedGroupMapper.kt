package com.teampatch.core.data.mapper

import com.teampatch.core.domain.model.group.InvitedGroup
import com.teampatch.core.network.model.group.response.GroupJoinResponse

internal fun GroupJoinResponse.toDomain(): InvitedGroup = InvitedGroup(
    groupId = group.groupId,
    groupManagerInfo = InvitedGroup.GroupManagerInfo(name = ""),
    users = group.userGroups.map {
        InvitedGroup.User(null)
    }
)