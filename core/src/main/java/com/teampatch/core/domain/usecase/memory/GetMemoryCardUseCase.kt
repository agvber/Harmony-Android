package com.teampatch.core.domain.usecase.memory

import com.teampatch.core.domain.model.memory.MemoryCard
import com.teampatch.core.domain.repository.MemoryCardRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class GetMemoryCardUseCase @Inject constructor(
    private val memoryCardRepository: MemoryCardRepository,
) {

    suspend operator fun invoke(memoryCardId: String): MemoryCard = memoryCardRepository.getMemoryCardById(memoryCardId).first()
}