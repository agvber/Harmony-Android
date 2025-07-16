package com.teampatch.core.domain.usecase.group

import com.teampatch.core.domain.entities.Group
import com.teampatch.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckIfGroupExistsUseCase @Inject constructor(
    private val group: Group,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Boolean {
        return runCatching {
            userRepository.getUserInfo().first().let {
                group.checkExistGroup(it.groupId)
            }
        }.getOrNull() == true
    }
}