package com.teampatch.core.domain.usecase.family

import com.teampatch.core.domain.repository.GroupManagementRepository
import javax.inject.Inject

class InviteFamilyUseCase @Inject constructor(
    private val groupManagementRepository: GroupManagementRepository,
) {

    suspend operator fun invoke(): String = groupManagementRepository.generateInviteCode()
}