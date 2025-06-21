package com.teampatch.core.domain.usecase.authentication

import com.teampatch.core.domain.repository.UserRepository
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsLoginRequiredUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    @OptIn(ObsoleteCoroutinesApi::class)
    operator fun invoke(): Flow<Boolean> = flow {
        userRepository.getUserInfo()
        emit(false)
    }
        .catch { it.printStackTrace(); emit(true) }
}