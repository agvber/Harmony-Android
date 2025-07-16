package com.teampatch.core.domain.usecase.authentication

import com.teampatch.core.domain.repository.AuthenticationRepository
import javax.inject.Inject

class LogoutAppUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {

    suspend operator fun invoke() {
        authenticationRepository.logout()
    }
}