package com.teampatch.core.domain.usecase.authentication

import com.teampatch.core.domain.exception.FamilyRegistrationRequiredException
import com.teampatch.core.domain.repository.AuthenticationRepository
import javax.inject.Inject

private const val FAMILY_REGISTRATION_REQUIRED_CODE: String = "-1"

class LoginKakaoUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {

    /**
     * @throws com.teampatch.core.domain.exception.FamilyRegistrationRequiredException 그룹이 존재 하지 않는 유저인 경우
     */

    suspend operator fun invoke() {
        val loginResult = authenticationRepository.loginKakao()

        if (loginResult.groupId == FAMILY_REGISTRATION_REQUIRED_CODE) {
            throw FamilyRegistrationRequiredException()
        }
    }
}