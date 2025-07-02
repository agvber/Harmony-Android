package com.teampatch.core.domain.usecase.user

import com.teampatch.core.domain.model.user.User
import com.teampatch.core.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<User> = userRepository.getUserInfo()
}