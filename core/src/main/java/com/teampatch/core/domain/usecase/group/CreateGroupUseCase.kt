package com.teampatch.core.domain.usecase.group

import com.teampatch.core.domain.repository.GroupManagementRepository
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val groupManagementRepository: GroupManagementRepository,
) {

    suspend operator fun invoke(
        vipName: String,
        vipAlias: String,
        managerName: String,
        managerRelation: String,
        managerProfileImageUri: String?
    ) {
        groupManagementRepository.createGroup(
            vipName = vipName,
            vipAlias = vipAlias,
            managerName = managerName,
            managerRelation = managerRelation,
            managerProfileImageUri = managerProfileImageUri
        )
    }
}