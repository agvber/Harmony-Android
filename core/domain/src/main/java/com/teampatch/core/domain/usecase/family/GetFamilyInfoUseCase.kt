package com.teampatch.core.domain.usecase.family

import com.teampatch.core.domain.model.FamilyInfo
import com.teampatch.core.domain.repository.GroupManagementRepository
import com.teampatch.core.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class GetFamilyInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val groupManagementRepository: GroupManagementRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<FamilyInfo>> = userRepository.getUserInfo().mapLatest { me ->
        groupManagementRepository.getUserGroupList(me.uid)
            .first { it.groupId.toInt() == me.groupId }.members
    }
}