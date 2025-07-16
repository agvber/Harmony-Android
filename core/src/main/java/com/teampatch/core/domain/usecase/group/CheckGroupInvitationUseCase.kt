package com.teampatch.core.domain.usecase.group

import com.teampatch.core.domain.entities.Group
import com.teampatch.core.domain.repository.GroupManagementRepository
import javax.inject.Inject

class CheckGroupInvitationUseCase @Inject constructor(
    private val group: Group,
    private val groupManagementRepository: GroupManagementRepository
) {
    suspend operator fun invoke(inviteCode: String) {
        group.checkInviteCode(inviteCode.toInt())
        groupManagementRepository.isGroupExist(inviteCode)
    }
}