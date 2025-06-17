package com.teampatch.core.domain.usecase.group

import com.teampatch.core.domain.repository.GroupManagementRepository
import javax.inject.Inject

class JoinGroupUseCase @Inject constructor(
    private val groupManagementRepository: GroupManagementRepository,
) {

    suspend operator fun invoke(
        memberName: String,
        vipRelation: String,
        memberProfileImageUri: String?,
        inviteCode: String
    ) {
        groupManagementRepository.joinGroup(
            memberName = memberName,
            vipRelation = vipRelation,
            memberProfileImageUri = memberProfileImageUri,
            inviteCode = inviteCode
        )
    }
}