package com.teampatch.core.domain.usecase.memory

import androidx.paging.PagingData
import com.teampatch.core.domain.model.memory.MemoryCard
import com.teampatch.core.domain.repository.MemoryCardRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetMemoryCardsUseCase @Inject constructor(
    private val memoryCardRepository: MemoryCardRepository,

) {

    operator fun invoke(): Flow<PagingData<MemoryCard>> = memoryCardRepository.getMemoryCards()
}