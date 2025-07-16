package com.teampatch.core.domain.usecase.profile

import com.teampatch.core.domain.repository.UserRepository
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(
        name: String?,
        profileImageUri: String?,
    ) {
        userRepository.editProfile(name, profileImageUri)
    }
}