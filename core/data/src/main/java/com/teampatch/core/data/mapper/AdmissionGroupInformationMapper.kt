package com.teampatch.core.data.mapper

import com.teampatch.core.data.database.model.GroupEntity
import com.teampatch.core.domain.model.AdmissionGroupInformation
import com.teampatch.core.domain.model.User

internal fun GroupEntity.toDomain(
    managerInformation: User,
    groupMembers: List<User>
): AdmissionGroupInformation {
    return AdmissionGroupInformation(
        groupId = id!!.toString(),
        manager = AdmissionGroupInformation.Manager(
            vipRelation = managerInformation.relation,
            name = managerInformation.name,
            profileImageUrl = managerInformation.profileImageUrl,
            uid = managerUid!!
        ),
        users = groupMembers
    )
}