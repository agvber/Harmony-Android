package com.teampatch.core.domain.usecase.memory

import com.teampatch.core.domain.repository.MemoryCardRepository
import com.teampatch.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import java.io.InputStream
import java.time.LocalDate
import javax.inject.Inject

class AddMemoryCardUseCase @Inject constructor(
    private val memoryCardRepository: MemoryCardRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        title: String,
        date: LocalDate,
        image: InputStream
    ) {
        val user = userRepository.getUserInfo().first()
        memoryCardRepository.addMemoryCard(title, date, image, user.uid)
    }
}