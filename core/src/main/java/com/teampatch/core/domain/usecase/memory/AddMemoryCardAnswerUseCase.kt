package com.teampatch.core.domain.usecase.memory

import com.teampatch.core.domain.repository.MemoryCardRepository
import javax.inject.Inject

class AddMemoryCardAnswerUseCase @Inject constructor(
    private val memoryCardRepository: MemoryCardRepository,
) {

    suspend operator fun invoke(memoryCardId: String, answer: String) {
        memoryCardRepository.addAnswer(memoryCardId, answer)
    }
}