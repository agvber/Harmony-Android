package com.teampatch.core.domain.usecase.group

import com.teampatch.core.domain.repository.GroupManagementRepository
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val groupManagementRepository: GroupManagementRepository,
) {

    suspend operator fun invoke() {
        groupManagementRepository.createFamilyGroup()
    }
}