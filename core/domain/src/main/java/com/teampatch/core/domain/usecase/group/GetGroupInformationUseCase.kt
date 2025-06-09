package com.teampatch.core.domain.usecase.group

import com.teampatch.core.domain.entities.Group
import com.teampatch.core.domain.model.AdmissionGroupInformation
import com.teampatch.core.domain.repository.GroupManagementRepository
import javax.inject.Inject

class GetGroupInformationUseCase @Inject constructor(
    private val group: Group,
    private val groupManagementRepository: GroupManagementRepository,
) {

    suspend operator fun invoke(inviteCode: String): AdmissionGroupInformation {
        group.checkInviteCode(inviteCode.toInt())
        return groupManagementRepository.searchGroup(inviteCode)
    }
}