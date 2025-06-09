package com.teampatch.core.data.repository

import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.domain.entities.TokenManager
import com.teampatch.core.domain.model.AdmissionGroupInformation
import com.teampatch.core.domain.model.InvitedGroup
import com.teampatch.core.domain.model.UserGroup
import com.teampatch.core.domain.repository.GroupManagementRepository
import com.teampatch.core.domain.repository.UserRepository
import com.teampatch.core.network.GroupRemoteDataSource
import com.teampatch.core.network.model.group.request.GroupCreationRequestBody
import com.teampatch.core.network.model.group.request.GroupJoinRequestBody
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class GroupManagementRepositoryImpl @Inject constructor(
    private val tokenManager: TokenManager,
    private val groupRemoteDataSource: GroupRemoteDataSource,
    private val userRepository: UserRepository,
) : GroupManagementRepository {

    override suspend fun createFamilyGroup(): String {
        val user = userRepository.getUserInfo().first()
        val body = GroupCreationRequestBody(
            userId = user.uid,
            name = user.name,
            deviceToken = tokenManager.getAccessToken()
        )
        val group = groupRemoteDataSource.createGroup(body)
        return group.inviteUrl
    }

    override suspend fun generateInviteCode(): String {
        val user = userRepository.getUserInfo().first()
        val response = groupRemoteDataSource.regenerateGroupInviteCode(user.groupId)
        return response.newInviteCode
    }

    override suspend fun joinFamilyGroup(inviteCode: String): InvitedGroup {
        val user = userRepository.getUserInfo().first()
        val body = GroupJoinRequestBody(
            userId = user.uid,
            inviteCode = inviteCode,
            deviceToken = tokenManager.getAccessToken()
        )
        val response = groupRemoteDataSource.joinGroup(body)
        return response.toDomain()
    }

    override suspend fun getUserGroupList(uid: String): List<UserGroup> {
        val response = groupRemoteDataSource.queryUserGroupList(uid)
        return response.groups.map { it.toDomain() }
    }

    override suspend fun queryGroupInvitationCode(): String {
        val user = userRepository.getUserInfo().first()
        val response = groupRemoteDataSource.queryGroupInvitationCode(user.groupId)
        return response.groupName
    }

    override suspend fun createGroup(
        vipName: String,
        vipAlias: String,
        managerName: String,
        managerRelation: String,
        managerProfileImageUri: String?
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun searchGroup(inviteCode: String): AdmissionGroupInformation {
        TODO("Not yet implemented")
    }

    override suspend fun isGroupExist(inviteCode: String): Boolean {
        TODO("Not yet implemented")
    }
}