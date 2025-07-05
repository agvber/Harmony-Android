package com.teampatch.core.domain.usecase.memory

import com.teampatch.core.domain.model.memory.MemoryCardQuestion
import com.teampatch.core.domain.repository.MemoryCardRepository
import javax.inject.Inject

class GetMemoryCardQuestionUseCase @Inject constructor(
    private val memoryCardRepository: MemoryCardRepository,
) {

    suspend operator fun invoke(
        memoryCardId: String,
    ): MemoryCardQuestion = memoryCardRepository.getQuestionMessage(memoryCardId)
}