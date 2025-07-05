package com.teampatch.core.domain.repository

import com.teampatch.core.domain.model.group.AdmissionGroupInformation
import com.teampatch.core.domain.model.group.InvitedGroup
import com.teampatch.core.domain.model.group.UserGroup

interface GroupManagementRepository {

    /**
     * @return Group 초대 URL
     */
    suspend fun createFamilyGroup(): String

    /**
     * Group 초대 코드를 재생성
     * @return Group 초대 코드
     */

    suspend fun generateInviteCode(): String

    suspend fun joinFamilyGroup(inviteCode: String): InvitedGroup

    suspend fun getUserGroupList(uid: String): List<UserGroup>

    /**
     * Group 초대 코드 조회
     * @return Group 초대 코드
     */
    suspend fun queryGroupInvitationCode(): String

    suspend fun createGroup(
        vipName: String,
        vipAlias: String,
        managerName: String,
        managerRelation: String,
        managerProfileImageUri: String?
    )

    suspend fun joinGroup(
        memberName: String,
        vipRelation: String,
        memberProfileImageUri: String?,
        inviteCode: String
    )

    suspend fun searchGroup(inviteCode: String): AdmissionGroupInformation

    suspend fun isGroupExist(inviteCode: String)
}